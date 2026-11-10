import assert from 'node:assert/strict';
import { test } from 'node:test';
import { publicationCoordinates, tagBranch } from './publish-coordinates.mjs';

const sha = 'c7bd861012345678901234567890123456789012';

test('versions and destinations for primary and feature branches', () => {
    for (const branch of ['main', 'master']) {
        assert.deepEqual(publicationCoordinates({ refType: 'branch', branch, sha }),
            { version: 'c7bd861', channel: 'snapshots', branch });
        assert.deepEqual(publicationCoordinates({ refType: 'tag', refName: '1.0', branch, sha }),
            { version: '1.0', channel: 'releases', branch });
    }
    for (const branch of ['multiplatform', 'feat/multiplatform', 'feat/engine/multiplatform']) {
        assert.deepEqual(publicationCoordinates({ refType: 'branch', branch, sha }),
            { version: 'c7bd861-multiplatform', channel: 'snapshots', branch });
        assert.deepEqual(publicationCoordinates({ refType: 'tag', refName: '1.0', branch, sha }),
            { version: '1.0-multiplatform', channel: 'snapshots', branch });
    }
});

test('a feature branch ending in main is not a primary branch', () => {
    assert.equal(publicationCoordinates({ refType: 'tag', refName: '1.0', branch: 'feat/main' }).version, '1.0-main');
    assert.equal(publicationCoordinates({ refType: 'tag', refName: '1.0', branch: 'feat/main' }).channel, 'snapshots');
});

test('tag selection prefers the first matching branch tip', () => {
    assert.equal(tagBranch(sha, (_, mode) => mode === 'points-at' ? ['feat/multiplatform', 'master'] : ['other']), 'feat/multiplatform');
});

test('tag selection falls back to the first containing branch', () => {
    assert.equal(tagBranch(sha, (_, mode) => mode === 'points-at' ? [] : ['dev', 'master']), 'dev');
    assert.throws(() => tagBranch(sha, () => []), /No remote branch contains/);
});

test('rejects input that would produce unsafe or invalid coordinates', () => {
    assert.throws(() => publicationCoordinates({ refType: 'tag', refName: 'release/1.0', branch: 'master' }), /Invalid tag/);
    assert.throws(() => publicationCoordinates({ refType: 'tag', refName: '1.0\nbad', branch: 'master' }), /Invalid tag/);
    assert.throws(() => publicationCoordinates({ refType: 'branch', branch: 'dev', sha: 'c7bd861' }), /full commit SHA/);
});
