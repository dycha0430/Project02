# 몰입캠프 2주차 여행 계획 앱 Traveler
## ABSTRACT
> 여러 친구들과 함께 여행을 갈 때, 모든 사람의 의견이 반영될 수 있는 계획을 짜는 것은 힘든 일이다.  
Traveler는 여러 사람이 함께 여행 계획을 편리하게 짤 수 있도록 도와주는 앱이다.

## Backend
> Link: https://github.com/MadCamp-Week2/Project02_backend
> #### Server framework : Django
> #### Database : Sqlite (Django에서 기본 제공)
>> 어플리케이션에서 사용하는 다음 항목에 대하여 모델을 구축함
- User: 이메일, 패스워드를 포함한 각 유저의 정보
- Place: 여행지
- Travel: 여행 정보
- Schedule: 각 여행에 포함되는 세부 스케줄들의 정보
- Profile: 앱 내 유저의 프로필 정보
> 안드로이드 클라이언트 측에서 retrofit을 통해 request를 보내면, 백엔드는 해당 request에 알맞게 데이터베이스를 수정하고, 필요 정보를 제공함.

## FUNCTION
### 카카오톡 로그인 기능
<img src="https://user-images.githubusercontent.com/68413811/148936083-f147fb37-c7a0-444e-a2b1-88f839f8609f.jpg"  width="270" height="600"/>
> 카카오톡 소셜로그인 sdk를 이용하여 사용자에 대한 카카오톡 프로필 및 부가 정보를 받으면, 해당 정보를 백엔드로 전송하여, 앱 계정을 생성함.  
> 생성된 앱 계정에 대해서는, 로그인 시 JWT 토큰을 발행하고 클라이언트 측으로 전송하여 정상적인 앱 사용이 가능하도록 함

### 여행 계획 홈/상세
홈 화면             |  계획 상세 화면             |  일정 넘기는 화면
:-------------------------:|:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/68413811/148860452-6d1b7485-cdaa-49a5-bc13-48eb33a5b242.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148860805-9c988307-f7dd-47b8-a220-fd7d6b4e54da.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148861327-cc0220f1-89df-4044-b632-b595daedab98.jpg"  width="270" height="600"/>


#### 홈 화면
> 내가 참여한 여행 계획들을 모아볼 수 있는 홈화면  
여행 제목, 장소, 날짜, 참여인원, 여행 상태를 볼 수 있음  
오른쪽 아래 floating button을 통해 계획 추가 가능  

#### 계획 상세 화면
> 홈 화면에서 특정 계획을 클릭하여 상세 화면을 띄울 수 있음  
상세 화면에서는 지도, 날짜 별 일정 목록을 볼 수 있음  
일정 목록은 날짜 별로 페이지가 있어, 넘겨서 각 날짜의 일정을 따로 관리할 수 있음. (View Pager2와 Recycler View를 함께 사용)

#### 계획 상세 화면 - 일정
일정 추가 화면 1             |  일정 추가 화면 2             
:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/68413811/148861687-494414f2-81b0-482b-a441-df9581a092c6.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148861712-e2ecaf31-db4d-4f4f-8cba-f3c837fb0376.jpg"  width="270" height="600"/>
> 일정 추가하기 버튼을 누르면 화면 1과 같이 직접 제목과 주소를 입력하여 일정을 추가할 수 있음  
지도의 마커를 누르면 화면 2와 같이 해당 위치에 대한 일정을 추가할 수 있음  
일정에는 추가적으로 일정에 필요한 돈과 간단한 메모를 입력할 수 있음

일정 추가 화면 3             |  일정 추가 화면 4             |  시간 선택 화면
:-------------------------:|:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/68413811/148862285-1e71492d-9332-4e0f-a76c-8e2f7bcef4b4.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148862727-cb4f3a55-1174-4ae9-b98c-873d79496b3f.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148861735-c7cc40aa-d3d7-4f57-9ef0-be6fd93b72ca.jpg"  width="270" height="600"/>
> 이미 추가된 일정들과 시간이 겹치면 화면 3과 같이 토스트 메세지가 뜨고 해당 시간에는 일정을 추가할 수 없음.
일정을 추가하거나 수정하면, 자동으로 그림 4와 같이 일정이 시간 순으로 정렬됨 (지도 3과 같은 일정에서 '이동하기' 일정을 추가한 것  
시간을 누르면 타임 피커를 통해 시간을 선택할 수 있고, 시작시간이 종료시간보다 늦도록 선택할 수 없음  

#### 계획 상세 화면 - 지도
지도 1             |  지도 2             |  지도 3
:-------------------------:|:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/68413811/148862151-56bff3f5-7f69-4641-be40-6aad0225177e.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148862355-a6a0254e-1a01-4d5e-be86-a0711e8640f2.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148862415-b292f613-de88-4deb-82c5-e33a31b81ff3.jpg"  width="270" height="600"/>
> Google Cloud Platform의 Maps SDK와 Places API를 사용함.  
아래 일정 칸에 들어있는 파란색 마커를 누르면, 지도 1의 사진처럼 해당 위치로 지도가 이동하고 빨간색 마커가 찍힘.  
지도 2의 사진처럼 스피너에서 레스토랑, 카페 등 원하는 종류를 누르면 지도에 표시된 마커들의 위치가 해당 종류의 위치들로 변경됨  
지도 3의 사진처럼 내가 추가한 일정의 경로가 빨간색 선으로 지도에 표시 됨.


#### 삭제 기능
일정 삭제 화면             |  계획 삭제 화면             
:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/68413811/148861830-c60a1a5c-5c53-4cc0-91d3-8f73983584d1.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148861862-5469cc91-91a7-485f-9efe-ed7380b69a4f.jpg"  width="270" height="600"/> 
> 롱 클릭을 통해 일정과 계획을 삭제할 수 있음

### 여행 계획 추가 
> 홈 화면에서 아래 floating button을 누르면 여행 계획을 새로 추가할 수 있다.

장소 선택 화면 1             |  장소 선택 화면 2             
:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/68413811/148893309-9bf4766b-f265-489c-9dc9-166fb4d9da06.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148893327-562a849f-1f7a-4148-b900-511694161072.jpg"  width="270" height="600"/> 
- 정해진 지역들 중 여행 갈 지역을 선택할 수 있음
- 지역을 선택하면 아래의 날짜 선택 화면으로 이동함

날짜 선택 화면             |  기타 선택 화면             
:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/68413811/148893691-117c2109-c071-4e8b-afde-e9e803184ec4.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148893721-da885514-c5e7-4a4b-90f3-890a53a481f5.jpg"  width="270" height="600"/> 
> 날짜 선택 화면에서는 Date Range Picker를 이용해서 시작 날짜와 종료 날짜를 선택할 수 있도록 함  
기타 선택 화면에서는 여행 제목을 입력하고 같이 여행에 참여할 친구를 초대할 수 있음

친구 초대 기능 1             |  친구 초대 기능 2             |  친구 초대 기능 3
:-------------------------:|:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/68413811/148893889-5a86c5ae-18f6-457d-8159-11ec8a035216.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148893894-74c12bce-0d32-43f3-bba4-1bf4ae1a9227.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148893900-0ace521a-cb6f-4acf-b869-a03ee2636334.jpg"  width="270" height="600"/>
> 아래 추가 버튼을 누르면 사진1 처럼 친구를 초대할 수 있는 란이 생성됨  
해당 칸을 누르면 사진 3과 같이 친구를 선택할 수 있는 다이얼로그가 뜨고 초대할 친구를 선택하면 사진3과 같이 추가가 완료됨.

### 친구 추가
친구 목록 화면             |  친구 추가 화면                |  친구 삭제 화면          
:-------------------------:|:-------------------------:|:-------------------------:
<img src="https://user-images.githubusercontent.com/68413811/148917191-22563818-34c0-471c-a2e0-44e0b9eaa173.jpg"  width="270" height="600"/>  |  <img src="https://user-images.githubusercontent.com/68413811/148917195-46f36c4b-c7b5-482b-b26c-a7ec78d9b1b8.jpg"  width="270" height="600"/>   |  <img src="https://user-images.githubusercontent.com/68413811/148917532-3766f293-2a3a-4aec-807e-1ca9552d6db0.jpg"  width="270" height="600"/> 
> 친구 목록 화면을 통해 나와 친구 추가한 친구의 목록을 볼 수 있음
> 오른쪽 아래 floating button을 누르면 친구 추가 다이얼로그가 뜨고 친구의 카카오톡 이메일을 입력하면 추가가 됨. 단, 친구가 앱에 가입한 상태여야 하며, 본인에게 친구 추가하거나 이미 추가된 친구인 경우에는 안내메세지가 뜨며 추가가 되지 않음.
> 롱 클릭을 통해 친구 삭제를 할 수 있음. 내가 삭제를 하면 나와 친구 모두에게서 삭제가 됨.
### 친구 요청 및 여행 초대 목록
// 사진 넣기
> 위의 친구 추가를 통해 친구 추가를 보내면 해당 친구에게는 해당 창을 통해 친구 요청이 오고 무시할 수도 있고 수락할 수도 있음.
> 여행을 만들 때 친구를 초대하면, 초대받은 친구들은 해당 창에 초대 요청이 뜨게 되며 이 또한 무시할 수 있고 수락할 수도 있음.
> 초대를 수락하면 내 여행 목록에 초대받은 여행이 추가 됨.

