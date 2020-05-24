# FloatingActivity
This library demonstrate an usage of Internal API that allow you to start an activity in floating mode.

![Screenshot_2020-05-23-07-10-18](https://user-images.githubusercontent.com/40482367/82717113-ff5c8d00-9cc4-11ea-8292-b6c1dceceb45.png)

## About this library
- It's simple, under 200kB APK for not using any Support Libraries or AndroidX.
- This put a real `Activity` to floating window running inside Service.
- This library using Internal API which requires a special Android SDK to build.
- This don't need a rooted device.

## Using this library
```java
import com.kdt.floatactivity.floatingact.FloatingIntent;


// Create a floating activity intent
FloatingIntent i = new FloatingIntent(MainActivity.this, FloatActivity.class);
i.startFloatingActivity();
```
**Change `FloatActivity.class` with your Activity want to make floating.**

## Current status
- This project including demo activity which not splited into test and library module.
- `Window` views are not packaged into one single `Layout` class.
- OpenGL rendering inside floating window is not yet tested.
- Resizing is not yet added.

## Limitations
- `EditText` outside window can't be input while window is showing.
- `SurfaceView` is not being hidden on minimize mode.
- Can't show a dialog inside floating window.

## Change in the future
- Use `android.app.ActivityView` which also a hidden API.

## License
- This library is licensed with Apache License v2.0.
