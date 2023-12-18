# 01. ITM Mobile Programming: Piece of Day
We have created a diary application.
We made a small diary app connected to a Spring server.
This application allows users to share their diaries with others nearby and write in a shared format.
Users can view and manage their diaries by daily and total list views.

- ITM Mobile Programming Team9
  - 황지환, 반영환, 정휘도


# 02. Screen Configuration
1. Splash
2. Google Authentication
3. Login
4. Daily Diary
5. Write Diary
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
- Coroutines
- Glide

# 04. Key Features
- Ability to write and manage diaries
- Provides a view of diaries written on selected dates
- Diary sharing using Bluetooth

# 05. Limitations
- Initially, we planned to implement Bump using Bluetooth, but due to the absence of Android devices and the difference in Bluetooth strength between devices, device selection was inaccurate. Therefore, we proceeded with Bluetooth sharing through Pairing, excluding the scanning process.
