# 01. 프로젝트에 대한 정보
Piece of Day
ITM Moblie Programming Team9

프로젝트 소개
우리는 다이어리 어플리케이션을 만들었습니다
스프링서버와 연결하는 작은 다이어리 어플은 만들었습니다
이 어플리케이션은 주변 모든이와 자신이 적인 일기를 공유해서 돌려쓰도록 할 수 있으며
자신의 일기를 일별, 전체 리스트별로 보고 관리 할 수 있습니다

# 02. 화면구성
1. Spalsh
2. Google Authentication
3. Login
4. DailyDiary
5. WriteDiary
6. ShareDiary
7. MyPage

# 03. 기능별 사용한 라이브러리
1. Google Authentication
- Google Oauth
- Firebase

2. Profile image
- Camera

3. WriteDiary
- Read & Create & Delete
- Location Sensor, Kakao API for address and weather API
- Chat Gpt Api for HashTags, GPT-4 DALL·E for Image

4. ShareDiary
- Bluetooth

5. Common 
- Retrofit
- Coroutines
- Glide

# 04. 주요기능
- 다이어리 작성 및 관리가능
- 선택 날짜별로 작성한 일기 뷰 제공
- 블루투스를 이용한 일기 공유

# 05. 한계점
- 처음에 블루투스를 이용한 Bump를 구현 하려고 했으나 안드로이드 기기의 부재 그리고 기기마다 블루투스 세기의 차이점으로 인해서 기기 선별이 부정확해 스캔과정을 제외한 Pairing으로 블루투스 공유를 진행했습니다
