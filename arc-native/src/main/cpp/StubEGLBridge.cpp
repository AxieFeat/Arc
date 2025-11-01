#include <jni.h>

extern "C" JNIEXPORT jlong JNICALL Java_arc_gles_window_NativeEGLBridge_getNativeLayer
  (JNIEnv* env, jclass clazz, jlong nsWindowPtr)
{
    return nsWindowPtr;
}
