"""
HearDay API Load Testing - Top 5 Articles by Demographic
연령대/성별 맞춤 인기 기사 Top 5 캐싱 성능 테스트
"""

from locust import HttpUser, task, between, events
from datetime import datetime
import threading

# 전역 공유 토큰 (최초 1회만 로그인)
SHARED_TOKEN = None
TOKEN_LOCK = threading.Lock()


def get_shared_token(client):
    """
    전역 공유 토큰 가져오기 (없으면 로그인)
    """
    global SHARED_TOKEN
    
    # 이미 토큰이 있으면 반환
    if SHARED_TOKEN:
        return SHARED_TOKEN
    
    # Lock을 사용해 첫 번째 사용자만 로그인
    with TOKEN_LOCK:
        # Lock 획득 후 다시 확인 (다른 스레드가 이미 토큰을 발급했을 수 있음)
        if SHARED_TOKEN:
            return SHARED_TOKEN
        
        try:
            response = client.post("/api/users/login", json={
                "email": "minki5@gmail.com",
                "password": "12345"
            }, name="[Login] POST /api/users/login")
            
            if response.status_code == 200:
                data = response.json()
                if data.get("success"):
                    SHARED_TOKEN = data["data"]["accessToken"]
                    print(f"\n{'='*80}")
                    print(f"✅ [공유 토큰 발급 완료]")
                    print(f"🔑 Token: {SHARED_TOKEN[:50]}...")
                    print(f"💡 모든 사용자가 이 토큰을 공유합니다")
                    print(f"{'='*80}\n")
                    return SHARED_TOKEN
                else:
                    print(f"❌ 로그인 실패 - Response: {data}")
            else:
                print(f"❌ 로그인 실패 - Status: {response.status_code}")
        except Exception as e:
            print(f"❌ 로그인 오류: {str(e)}")
        
        return None


class Top5ArticlesUser(HttpUser):
    """
    Top 5 Articles by Demographic API 테스트
    - 캐싱 효과 측정에 최적화
    - 모든 사용자가 하나의 토큰을 공유
    """
    
    wait_time = between(1, 2)
    
    def on_start(self):
        """
        공유 토큰 가져오기 (최초 1회만 로그인)
        """
        # 전역 공유 토큰 사용
        pass
    
    def get_headers(self):
        """인증 헤더 반환 (공유 토큰 사용)"""
        token = get_shared_token(self.client)
        if token:
            return {
                "Authorization": f"Bearer {token}",
                "Content-Type": "application/json"
            }
        return {"Content-Type": "application/json"}
    
    @task
    def get_top_articles_by_demographic(self):
        """
        [테스트 대상] 연령대/성별 맞춤 인기 기사 Top 5 조회
        """
        with self.client.get(
            "/api/articles/top-by-demographic",
            headers=self.get_headers(),
            catch_response=True,
            name="GET /api/articles/top-by-demographic"
        ) as response:
            if response.status_code == 200:
                try:
                    data = response.json()
                    if data.get("success"):
                        article_count = len(data.get("data", []))
                        if article_count > 0:
                            response.success()
                        else:
                            response.failure("No articles returned")
                    else:
                        response.failure("API returned success=false")
                except Exception as e:
                    response.failure(f"JSON parsing error: {str(e)}")
            elif response.status_code == 401:
                response.failure("Unauthorized - 인증 실패")
            else:
                response.failure(f"HTTP {response.status_code}")


# 테스트 시작/종료 이벤트
@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    print("\n" + "="*80)
    print("🎯 [TEST] Top 5 Articles by Demographic API")
    print(f"📅 시작 시간: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print(f"🌐 Target: {environment.host}")
    print(f"🔐 로그인: 최초 1회만 수행 (토큰 공유)")
    print("="*80 + "\n")


@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    print("\n" + "="*80)
    print("✅ [완료] Top 5 Articles by Demographic Test")
    print(f"📅 종료 시간: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("="*80 + "\n")
