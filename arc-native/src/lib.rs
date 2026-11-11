#![allow(non_snake_case)]

use std::ffi::c_void;

#[cfg(target_os = "macos")]
mod macos {
    use std::ffi::{c_char, c_void};

    #[link(name = "objc")]
    extern "C" {
        fn objc_getClass(name: *const c_char) -> *mut c_void;
        fn sel_registerName(name: *const c_char) -> *mut c_void;
        fn objc_msgSend();
    }

    #[link(name = "Cocoa", kind = "framework")]
    #[link(name = "QuartzCore", kind = "framework")]
    extern "C" {}

    pub unsafe fn native_layer(window: i64) -> i64 {
        let send_object: unsafe extern "C" fn(*mut c_void, *mut c_void) -> *mut c_void =
            std::mem::transmute(objc_msgSend as *const ());
        let send_bool: unsafe extern "C" fn(*mut c_void, *mut c_void, i8) =
            std::mem::transmute(objc_msgSend as *const ());
        let send_set_object: unsafe extern "C" fn(*mut c_void, *mut c_void, *mut c_void) =
            std::mem::transmute(objc_msgSend as *const ());

        let view = send_object(
            window as usize as *mut c_void,
            sel_registerName(b"contentView\0".as_ptr().cast()),
        );
        send_bool(
            view,
            sel_registerName(b"setWantsLayer:\0".as_ptr().cast()),
            1,
        );
        let class = objc_getClass(b"CAMetalLayer\0".as_ptr().cast());
        let layer = send_object(class, sel_registerName(b"layer\0".as_ptr().cast()));
        send_set_object(
            view,
            sel_registerName(b"setLayer:\0".as_ptr().cast()),
            layer,
        );
        layer as usize as i64
    }
}

#[no_mangle]
pub extern "system" fn Java_arc_window_egl_NativeEGLBridge_getNativeLayer(
    _env: *mut c_void,
    _class: *mut c_void,
    window: i64,
) -> i64 {
    #[cfg(target_os = "macos")]
    unsafe {
        macos::native_layer(window)
    }
    #[cfg(not(target_os = "macos"))]
    {
        window
    }
}
