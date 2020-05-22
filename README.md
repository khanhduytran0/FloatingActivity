# FloatingActivity
This library demonstrate an usage of Internal API that allow you to start an activity in floating mode.

## About this library
- It's simple, under 200kB APK for not using any Support Libraries or AndroidX.
- This put a real `Activity` to floating window running inside Service.
- This library using non-public API which requires a special Android SDK to build.
- This don't need a rooted device.

## Current status
- This project including demo activity which not splited into test and library module.
- `Window` views are not packaged into one single `Layout` class.
- `SurfaceView` is not being hidden on minimize mode.
- OpenGL rendering inside floating window is not yet tested.
- Resizing is not yet added.

## License
- This library is licensed with Apache License v2.0.
