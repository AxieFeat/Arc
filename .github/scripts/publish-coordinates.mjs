import { appendFileSync } from 'node:fs';
import { execFileSync } from 'node:child_process';
import { pathToFileURL } from 'node:url';

const primaryBranches = new Set(['main', 'master']);

function checkComponent(value, name) {
    if (!value || /[\r\n\\/]/.test(value)) {
        throw new Error(`Invalid ${name}: ${JSON.stringify(value)}`);
    }
    return value;
}

export function publicationCoordinates({ refType, refName, branch, sha }) {
    if (refType !== 'branch' && refType !== 'tag') {
        throw new Error(`Unsupported ref type: ${refType}`);
    }
    if (!branch || /[\r\n]/.test(branch)) throw new Error('A source branch is required');
    const branchSuffix = checkComponent(branch.split('/').at(-1), 'branch suffix');
    const isPrimary = primaryBranches.has(branch);
    let version;
    if (refType === 'tag') {
        const tag = checkComponent(refName, 'tag');
        version = isPrimary ? tag : `${tag}-${branchSuffix}`;
    } else {
        if (!/^[0-9a-f]{40,64}$/i.test(sha || '')) throw new Error('A full commit SHA is required');
        const shortSha = sha.slice(0, 7);
        version = isPrimary ? shortSha : `${shortSha}-${branchSuffix}`;
    }
    return { version, channel: refType === 'tag' && isPrimary ? 'releases' : 'snapshots', branch };
}

function findBranches(commit, mode) {
    const output = execFileSync('git', [
        'for-each-ref', `--${mode}=${commit}`,
        '--format=%(refname:strip=3)%09%(symref)', 'refs/remotes/origin/'
    ], { encoding: 'utf8' });
    return output.split(/\r?\n/).filter(Boolean).map(line => line.split('\t'))
        .filter(([name, symbolic]) => name !== 'HEAD' && !symbolic)
        .map(([name]) => name);
}

export function tagBranch(commit, find = findBranches) {
    // Prefer branch tips; an older tagged commit may belong to several branches.
    const candidates = find(commit, 'points-at');
    const branches = candidates.length ? candidates : find(commit, 'contains');
    if (!branches.length) {
        throw new Error('No remote branch contains the tagged commit. Run this workflow manually with the branch and tag.');
    }
    return branches[0];
}

function main() {
    const manual = process.env.GITHUB_EVENT_NAME === 'workflow_dispatch';
    const refType = manual ? 'tag' : process.env.GITHUB_REF_TYPE;
    const refName = manual ? process.env.INPUT_TAG : process.env.GITHUB_REF_NAME;
    const sha = execFileSync('git', ['rev-parse', 'HEAD'], { encoding: 'utf8' }).trim();
    const branch = manual ? process.env.INPUT_BRANCH : refType === 'tag' ? tagBranch(sha) : refName;
    if (manual) {
        if (!branch || /[\r\n]/.test(branch)) throw new Error('A source branch is required');
        const ref = `refs/remotes/origin/${branch}`;
        execFileSync('git', ['show-ref', '--verify', '--quiet', ref]);
        execFileSync('git', ['merge-base', '--is-ancestor', sha, ref]);
    }
    const coordinates = publicationCoordinates({ refType, refName, branch, sha });
    appendFileSync(process.env.GITHUB_OUTPUT,
        `version=${coordinates.version}\nchannel=${coordinates.channel}\nbranch=${coordinates.branch}\n`);
    console.log(`Publishing ${coordinates.version} from ${coordinates.branch} to ${coordinates.channel}`);
}

if (process.argv[1] && import.meta.url === pathToFileURL(process.argv[1]).href) {
    try {
        main();
    } catch (error) {
        console.error(`Cannot determine publication coordinates: ${error.message}`);
        process.exitCode = 1;
    }
}
