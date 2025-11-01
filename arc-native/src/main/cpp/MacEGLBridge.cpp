#include <Cocoa/Cocoa.h>
#include <QuartzCore/CAMetalLayer.h>
#include <jni.h>

extern "C" JNIEXPORT jlong JNICALL Java_arc_gles_window_NativeEGLBridge_getNativeLayer
  (JNIEnv* env, jclass clazz, jlong nsWindowPtr)
{
    NSWindow* nswin = reinterpret_cast<NSWindow*>(static_cast<uintptr_t>(nsWindowPtr));
    NSView* view = [nswin contentView];

    [view setWantsLayer:YES];
    CAMetalLayer* metalLayer = [CAMetalLayer layer];
    [view setLayer:metalLayer];

    return static_cast<jlong>(reinterpret_cast<uintptr_t>(metalLayer));
}
