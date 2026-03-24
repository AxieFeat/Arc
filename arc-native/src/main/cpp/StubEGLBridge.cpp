// We defines only needed JNI types because on Windows we don't have access to jni.h
#if defined(_WIN32)
#define JNIEXPORT __declspec(dllexport)
#define JNICALL __stdcall
#else
#define JNIEXPORT __attribute__ ((visibility ("default")))
#define JNICALL
#endif

typedef long long jlong;
typedef void* jclass;
typedef void* JNIEnv;

extern "C" JNIEXPORT jlong JNICALL Java_arc_gles_window_NativeEGLBridge_getNativeLayer
  (JNIEnv* env, jclass clazz, jlong nsWindowPtr)
{
    return nsWindowPtr;
}
