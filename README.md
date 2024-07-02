![배경](https://github.com/CosineAndroid/GroupFinder/assets/100404990/e37e774c-f5f8-4eae-bce8-7305139c1f6e)  

# 띱 - 리그 오브 레전드 그룹 찾기
#### 💡 리그 오브 레전드를 같이할 그룹을 찾아보세요!  
> 🎮 태그 검색을 통해 자신의 성향과 맞는 그룹을 찾을 수 있습니다.
  
> 🕹️ 방을 생성하여 필요한 라인을 선택 후 같이할 사람을 찾을 수 있습니다.

## 개발 기간
> 🕛 2024년 5월 27일 ~ 2024년 7월 5일

## 기술 스택
| 분류 | 이름 |
| --- | --- |
| Architecture | <img src="https://img.shields.io/badge/Clean Architecture-b311c2"> <img src="https://img.shields.io/badge/MVVM-b311c2"> |
| Jetpack | <img src="https://img.shields.io/badge/Compose-11c29c"> <img src="https://img.shields.io/badge/Hilt-11c29c"> <img src="https://img.shields.io/badge/ViewModel-11c29c"> <img src="https://img.shields.io/badge/LiveData-11c29c"> <img src="https://img.shields.io/badge/ViewBinding-11c29c"> <img src="https://img.shields.io/badge/LifeCycle-11c29c"> |
| 비동기 처리 | <img src="https://img.shields.io/badge/Coroutine-29456C"> <img src="https://img.shields.io/badge/Flow-29456C"> |
| 데이터 처리 | <img src="https://img.shields.io/badge/SharedPreferences-89632A"> <img src="https://img.shields.io/badge/Gson-89632A"> |
| Firebase | <img src="https://img.shields.io/badge/Firestore-4285F4"> <img src="https://img.shields.io/badge/Functions-4285F4"> <img src="https://img.shields.io/badge/Messaging-4285F4"> |
| API 통신 | <img src="https://img.shields.io/badge/Retrofit-373737"> <img src="https://img.shields.io/badge/OkHttp-373737">  |
| 타사 API | <img src="https://img.shields.io/badge/Riot(League of Legends)-03C75A"> |
| 이미지 로더 | <img src="https://img.shields.io/badge/Coil-18BED4"> |
| UI Framework | <img src="https://img.shields.io/badge/Fragment-492f64"> <img src="https://img.shields.io/badge/RecyclerView-492f64"> <img src="https://img.shields.io/badge/XML-492f64"> <img src="https://img.shields.io/badge/ViewPager-492f64"> <img src="https://img.shields.io/badge/SwipeRefreshLayout-492f64"> <img src="https://img.shields.io/badge/Flexbox-492f64"> |

## 개발자 및 담당한 기능
### 팀장
* [공지훈](https://github.com/Cosine-A)  
  * 게시글 목록
  * 회원가입/로그인
  * 프로필
  * 공지사항
  * 차단/신고(유저, 게시글)
  * 유저 정보/전적 - [Riot API](https://developer.riotgames.com/apis)
  * Firestore
### 부팀장
* [권민찬](https://github.com/Sth-bear)
  * 게시글 세부
  * Functions
  * Messaging
### 팀원
* [김보라](https://github.com/bora44144)
  * 프로필 UI
  * QA
* [안진혁](https://github.com/AnJinHyuck)
  * 게시글 작성
* [마해인](https://github.com/godls20455)
  * 태그 바텀시트

## 기능
| [회원가입] | [로그인] |
| --- | --- |
| - 아이디 입력<br>- 비밀번호 입력<br>- 닉네임 입력<br>- 태그 입력<br>- 약관 동의 | - 아이디 입력<br>- 비밀번호 입력<br>- 자동 로그인 |

| [게시글 목록] | [게시글 작성] | [게시글 세부] | [태그 바텀시트] | [소환사(유저) 전적] |
| --- | --- | --- | --- | --- |
| - 게시물 목록 확인<br>- 태그를 통한 게시물 검색<br>- 게시글 카테고리 변경 | - 제목 입력<br>- 본문 입력<br>- 태그 선택<br>- 라인 선택  | - 방장 전적 확인<br>- 라인 참가<br>- 게시글 신고/삭제<br>- 작성자 신고/차단 | - 태그 선택 | - 플레이한 Top3 챔피언 확인<br>- 랭크 티어 확인 |

| [프로필] | [공지사항] | [차단한 유저] |
| --- | --- | --- |
| - 닉네임#태그 변경<br>- 참가 중인 방 확인<br>- 공지사항<br>- 차단한 유저<br>- 로그아웃/탈퇴 | - 확인<br>- 작성(관리자만) | - 확인<br>- 해제 |

## 와이어프레임
![image](https://github.com/CosineAndroid/GroupFinder/assets/100404990/92f61f36-e43c-482d-8882-e630d263c1dc)

## 실제 앱 화면
<img width="200" alt="1" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/4a114403-0fdf-498e-b43c-96a0d69f8d53">
<img width="200" alt="2" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/9f54cb7f-23fe-42fe-ae8f-64dd6828d22f">
<img width="200" alt="3" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/2e7b71f5-fbd1-4c12-b1f3-ac513453ea7d">
<img width="200" alt="4" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/16c39754-321b-40da-8e8d-e5bb3543e339">
<img width="200" alt="5" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/b34b1473-9d0c-4add-b82e-af18d3b50d1f">
<img width="200" alt="6" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/48dc2b7f-85ba-4f7e-887c-e77cc1838f4a">
<img width="200" alt="7" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/6756f62d-5ec9-47fb-a5d7-03e36baf4b45">
<img width="200" alt="8" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/9446efb4-e2ec-44e9-8c88-7bae9026f367">
<img width="200" alt="9" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/9aa9b874-65f8-4f82-a693-159769695f69">
<img width="200" alt="10" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/7f3c2eeb-9dbb-4f2b-936c-6b2a37b0f675">

## Git 전략
* 기능별로 브랜치를 나누었습니다.
* 각 기능마다 issue를 작성하여 진행 상황을 한번에 알아볼 수 있도록 하였습니다.
* 커밋과 풀 리퀘스트를 작성 후 팀장의 승인으로 merge 하였습니다.
* 완성 후 최종적으로 main 브랜치로 옮겨 배포하였습니다.  
<img width="300" alt="1" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/a192e56b-6f68-410f-a2d7-00fd3b83149b">
<img width="450" alt="3" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/4ba0cf24-7ba3-4f6b-857b-c78e0803bb8e">  
<img width="700" alt="2" src="https://github.com/CosineAndroid/GroupFinder/assets/100404990/b0db1a0a-51ad-4c28-a923-5e02a90fbb70">  

## 트러블 슈팅
### 여러 조건으로 리스트 정렬
* 문제점: 아래처럼 정렬 메서드가 따로 있으면 결국엔 마지막을 기준으로 정렬됨
```kotlin
@ViewModelScoped
class ListUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(mode: Mode, tags: List<String>): List<PostModel> {
        return postRepository.getPosts(tags).filter {
            it.getMode() == mode
        }.sortedByDescending {
            it.time
        }.sortedBy {
            it.getJoinedPeopleCount() == it.getTotalPeopleCount()
        }
    }
}
```
* 해결방법: 이를 해결하기 위해 sortedWith, compareBy, thenByDescending 이 3개를 활용하여 해결함
```kotlin
@ViewModelScoped
class ListUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(mode: Mode, tags: List<String>): List<PostModel> {
        return postRepository.getPosts(tags).filter {
            it.getMode() == mode
        }.sortedWith(
            compareBy<PostModel> {
                // 참가 중인 인원 == 총 필요한 인원
                it.getJoinedPeopleCount() == it.getTotalPeopleCount()
            }.thenByDescending {
                it.time
            }
        )
    }
}
```
### TextField에서 영문 자판 먼저 올라오게
* 문제점: 아이디 같은 경우에는 영문 자판이 먼저 올라와야 하는데 기기에 기본값을 가져오기 때문에 한글 자판이 먼저 올라옴
* 해결방법: Compose는 BasicTextField의 keyboardOptions에 platformImeOptions가 있었고 해당 옵션에 **"defaultInputmode=english;"** 을 넣어주니 영문 자판이 올라왔다.
```kotlin
BasicTextField(
    keyboardOptions = KeyboardOptions.Default.copy(
        platformImeOptions = PlatformImeOptions("defaultInputmode=english;")
    )
)
```
