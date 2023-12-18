<img width="200" alt="스크린샷 2023-12-18 오후 2 24 27" src="https://github.com/ITM-Mobile-Programming/Front/assets/119919849/55c6eb58-50b9-4fb5-91c5-183ff531e302">

# 01. ITM Mobile Project: Piece of Day
> We have created a diary application.
> We made a small diary app connected to a Spring server.
> This application allows users to share their diaries with others nearby and write in a shared format.
> Users can view and manage their diaries by daily and total list views.

> team9 : 황지환, 정휘도, 반영환

# 02. Screen Configuration

<img width="499" alt="image" src="https://github.com/ITM-Mobile-Programming/Front/assets/119919849/6a309d30-0ccb-4a05-b557-73e60e54cafa">
1. Splash
2. Google Authentication
3. Login
<img width="828" alt="image" src="https://github.com/ITM-Mobile-Programming/Front/assets/119919849/5f120145-b23d-4a72-b11b-c48bc2fc8c05"> 
4. Daily Diary
5. Write Diary
<img width="219" alt="image" src="https://github.com/ITM-Mobile-Programming/Front/assets/119919849/bf2d730c-9380-4d40-bb09-22ab80fc00da">  
6. Share Diary
7. My Page

# 03. Libraries & Feature Used for Each Feature
1. Google Authentication
- Google OAuth
- Firebase
- SharedPreferences

2. Profile Image
- Camera

3. Write Diary
- Location Sensor, Kakao API for address, and weather API
- Chat GPT Api for HashTags, GPT-4 DALL·E for Image

4. Share Diary
- Bluetooth

5. Common
- Retrofit
- Glide

# 04. Key Features
- Ability to write and manage diaries
- Provides a view of diaries written on selected dates
- Diary sharing using Bluetooth

# 05. Limitations
- Initially, we planned to implement Bump using Bluetooth, but due to the absence of Android devices and the difference in Bluetooth strength between devices, device selection was inaccurate. Therefore, we proceeded with Bluetooth sharing through Pairing, excluding the scanning process.
