import requests
import pandas as pd

KAKAO_REST_API_KEY = "76fda482da307699d5ebdb9e24f8a175"

def geocode_address(address):
    url = "https://dapi.kakao.com/v2/local/search/address.json"
    headers = {"Authorization" : f"KakaoAK {KAKAO_REST_API_KEY}"}
    params = {"query" : address}

    response = requests.get(url, headers=headers, params=params)

    print("주소 -> 좌표 지오코딩 시도...")

    if response.status_code == 200:
        result = response.json()

        if result['documents']:
            point_info = result['documents'][0]
            longitude = point_info['x']
            latitude = point_info['y']
            
            print("[성공] 좌표 변환 완료")
            return longitude, latitude
        else:
            print("[실패] 존재하지 않는 주소.")
            return None, None
    else:
        print("API 호출 실패")
        return None, None
    
