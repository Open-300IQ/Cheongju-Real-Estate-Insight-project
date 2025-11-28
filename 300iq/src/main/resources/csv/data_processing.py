from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, NoSuchElementException, StaleElementReferenceException
import time
import pandas as pd
import re
import os 
import geocoding

# ----------------------------------------------------
#  청주시의 주요 행정동 리스트
# ----------------------------------------------------
CHEOJNU_DONG_LIST = [
"청주시 상당구 남문로1가",
"청주시 상당구 남문로2가",
"청주시 상당구 북문로1가",
"청주시 상당구 북문로2가",
"청주시 상당구 남주동",
"청주시 상당구 문화동",
"청주시 상당구 서문동",
"청주시 상당구 석교동",
"청주시 상당구 수동",
"청주시 상당구 영동",
"청주시 상당구 우암동",
"청주시 상당구 내덕동",
"청주시 상당구 율량동",
"청주시 상당구 사천동",
"청주시 상당구 탑동",
"청주시 상당구 금천동",
"청주시 상당구 용담동",
"청주시 상당구 명암동",
"청주시 상당구 용정동",
"청주시 상당구 방서동",
"청주시 상당구 산성동",
"청주시 상당구 운동동",
"청주시 상당구 월오동",
"청주시 상당구 지북동",
"청주시 상당구 평촌동",
"청주시 상당구 동남동",
"청주시 상당구 낭성면",
"청주시 상당구 미원면",
"청주시 상당구 가덕면",
"청주시 상당구 남일면",
"청주시 상당구 문의면",
"청주시 서원구 모충동",
"청주시 서원구 사직동",
"청주시 서원구 사창동",
"청주시 서원구 산남동",
"청주시 서원구 분평동",
"청주시 서원구 수곡동",
"청주시 서원구 미평동",
"청주시 서원구 장성동",
"청주시 서원구 성화동",
"청주시 서원구 개신동",
"청주시 서원구 죽림동",
"청주시 서원구 현도면",
"청주시 서원구 남이면",
"청주시 흥덕구 봉명동",
"청주시 흥덕구 운천동",
"청주시 흥덕구 신봉동",
"청주시 흥덕구 복대동",
"청주시 흥덕구 가경동",
"청주시 흥덕구 강서동",
"청주시 흥덕구 송정동",
"청주시 흥덕구 비하동",
"청주시 흥덕구 정봉동",
"청주시 흥덕구 지동동",
"청주시 흥덕구 향정동",
"청주시 흥덕구 신성동",
"청주시 흥덕구 석곡동",
"청주시 흥덕구 휴암동",
"청주시 흥덕구 현암동",
"청주시 흥덕구 동막동",
"청주시 흥덕구 수의동",
"청주시 흥덕구 외북동",
"청주시 흥덕구 신촌동",
"청주시 흥덕구 서촌동",
"청주시 흥덕구 장암동",
"청주시 흥덕구 원평동",
"청주시 흥덕구 오송읍",
"청주시 흥덕구 강내면",
"청주시 흥덕구 옥산면",
"청주시 청원구 사천동",
"청주시 청원구 율량동",
"청주시 청원구 정하동",
"청주시 청원구 정상동",
"청주시 청원구 정북동",
"청주시 청원구 주중동",
"청주시 청원구 주성동",
"청주시 청원구 오동동",
"청주시 청원구 외남동",
"청주시 청원구 오창읍",
"청주시 청원구 내수읍",
"청주시 청원구 북이면"
]

final_scraped_data = []

# ---  가격 파싱 전용 함수 (정규식 및 단위 변환) ---

def convert_korean_price_to_manwon(price_str):
    """'5억 5000' -> '55000', '5000' -> '5000' (만원 단위 숫자 문자열)로 변환"""
    # 쉼표, 공백, '원', '만' 제거
    cleaned = price_str.replace(',', '').replace(' ', '').replace('원', '').replace('만', '')

    # 1. '억' 단위 처리
    if '억' in cleaned:
        parts = cleaned.split('억')
        eok_str = parts[0] if parts[0] else '0'
        cheon_str = parts[1].replace('천', '') if len(parts) > 1 and parts[1] else '0'
        
        eok_value = int(eok_str) * 10000
        # 억 뒤에 숫자가 없으면 0으로 처리
        cheon_val_clean = re.sub(r'[^0-9]', '', cheon_str)
        cheon_value = int(cheon_val_clean) if cheon_val_clean else 0
        
        return str(eok_value + cheon_value)
    
    # 2. '천' 단위 처리
    if '천' in cleaned:
        return cleaned.replace('천', '').strip()
        
    # 3. 기타 (숫자만 있는 경우)
    return cleaned

def parse_preview_price(preview_info):
    """preview_info (예: '매매 5억 5000', '월세 500/50')를 파싱"""
    
    transaction_type = "아파트(기타)"
    price_value = "0"
    rent_value = "0"
    
    clean_text = preview_info.replace('\n', ' ').strip()
    
    # 정규식으로 '거래유형'과 '나머지 가격 문자열' 분리
    match = re.search(r'(매매|전세|월세)\s*(.+)', clean_text)
    if not match:
        return price_value, rent_value, transaction_type 

    tr_type_kr = match.group(1)
    price_part = match.group(2).strip()
    
    transaction_type = f"아파트({tr_type_kr})"

    if tr_type_kr == '월세':
        # 월세는 '보증금/월세' 또는 '보증금 / 월세' 형태
        if '/' in price_part:
            deposit_str, rent_str = map(str.strip, price_part.split('/', 1))
            price_value = convert_korean_price_to_manwon(deposit_str)
            rent_value = convert_korean_price_to_manwon(rent_str)
        else:
            # '/'가 없는 경우 (보증금만 있는 특이 케이스 등)
            price_value = convert_korean_price_to_manwon(price_part)
            rent_value = "0"
    
    elif tr_type_kr in ['매매', '전세']:
        price_value = convert_korean_price_to_manwon(price_part)
        rent_value = "0"
        
    return price_value, rent_value, transaction_type

# --- WebDriver 초기 설정 및 접속 ---
options = webdriver.ChromeOptions()
driver = webdriver.Chrome(options=options)
driver.maximize_window()
driver.get('https://www.dabangapp.com/map/apt')
print(" 1. 다방 지도 페이지 접속 완료.")

# 검색창 로딩 대기
try:
    WebDriverWait(driver, 15).until(
        EC.presence_of_element_located((By.ID, 'search-input'))
    )
except TimeoutException:
    print("❌ 초기 로딩 시간 초과: 검색창을 찾을 수 없습니다.")
    driver.quit()
    exit()

# ----------------------------------------------------
#  메인 루프: 동별 검색 및 스크래핑 실행
# ----------------------------------------------------

for dong_name in CHEOJNU_DONG_LIST:
    print(f"\n---  {dong_name} 검색 시작 ---")
    
    try:
        # 1. 검색창 초기화 및 검색어 입력
        search_box = driver.find_element(By.ID, 'search-input')
        search_box.clear()
        search_box.send_keys(dong_name)

        time.sleep(1) 
        driver.find_element(By.TAG_NAME, 'body').send_keys(Keys.ESCAPE)
        # 2. 자동 완성 목록에서 첫 번째 항목 클릭
        autocomplete_button_selector = 'div.sc-fgnnHK button'
        first_autocomplete_item = WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, autocomplete_button_selector))
        )
        first_autocomplete_item.click()
        print(f" {dong_name}: 검색 및 지도 이동 완료.")
        
        # 3. '아파트' 탭 재확정
        time.sleep(2)

        # ----------------------------------------------------
        #  4. 페이지네이션 루프 시작
        # ----------------------------------------------------
        page_num = 1
        while True: 
            print(f"\n---  페이지 {page_num} 스크래핑 시작 ---")
            
            time.sleep(2)
            item_selector = '#apt-list li'
            try:
                WebDriverWait(driver, 10).until(
                    EC.presence_of_element_located((By.CSS_SELECTOR, item_selector))
                )
            except TimeoutException:
                if page_num == 1:
                    print(f" {dong_name}: 페이지 1에서 매물이 없어 스크래핑을 건너뜁니다.")
                else:
                    print(f" {dong_name}: 페이지 {page_num}에서 매물이 없어 페이지네이션을 종료합니다.")
                break 
            
            property_items = driver.find_elements(By.CSS_SELECTOR, item_selector)
            if not property_items:
                break
                
            print(f" {dong_name}, 페이지 {page_num}: 총 **{len(property_items)}개** 매물 확인.")
            
            
            # --- 5. 개별 매물 클릭 및 상세 정보 추출 루프 ---
            for i in range(len(property_items)):
                try:
                    # 매물 항목 리스트 다시 찾기 (Stale 방지)
                    item_to_click = driver.find_elements(By.CSS_SELECTOR, item_selector)[i]
                    
                    # 1. 클릭 전 스크롤 및 강제 클릭
                    driver.execute_script("arguments[0].scrollIntoView({block: 'center'});", item_to_click)
                    time.sleep(0.5) 
                    
                    # preview_info에서 추출
                    preview_info = item_to_click.find_element(By.TAG_NAME, 'h1').text.replace('\n', ' ').strip()
                    
                    # 파싱 함수 호출 (여기서 가격, 월세, 유형 결정)
                    price_value, rent_value, transaction_type = parse_preview_price(preview_info)
                    
                    print(f"   [매물 {i+1}/{len(property_items)}]: {preview_info} (파싱: {price_value}/{rent_value}) 클릭...")

                    # 2. 매물 클릭 (상세 페이지 열기 - 주소/면적 등을 위해)
                    driver.execute_script("arguments[0].click();", item_to_click)
                    
                    time.sleep(0.1)
                    driver.find_element(By.TAG_NAME, 'body').send_keys(Keys.ESCAPE)

                    # 상세 페이지 헤더 로드 대기 (주소/면적 추출에 필요)
                    price_header_xpath = "//*[@id='container-room-root']//section[1]//h1"
                    WebDriverWait(driver, 10).until(
                        EC.presence_of_element_located((By.XPATH, price_header_xpath))
                    )
                    time.sleep(0.3)
                    
                    # ---  데이터 추출 (주소 및 면적) ---
                    
                    # (1) 주소
                    address_xpath = "//section[@data-scroll-spy-element='near']//p[1]" 
                    address_element = driver.find_element(By.XPATH, address_xpath)
                    raw_address = address_element.text.strip()
                    
                    # (2) 전용면적
                    area_value = "0"
                    try:
                        detail_section = driver.find_element(By.CSS_SELECTOR, "section[data-scroll-spy-element='detail-info']")
                        detail_text = detail_section.text
                        area_match = re.search(r'전용/공급면적\s*(\d+\.?\d*)㎡', detail_text) 
                        if area_match:
                            area_value = area_match.group(1)
                    except:
                        area_value = "0"

                    latitude, longitude = geocoding.geocode_address(raw_address)

                    # --- 최종 데이터 저장 ---
                    final_scraped_data.append({
                        'address': raw_address,
                        'area': area_value,
                        'price': price_value,
                        'rent': rent_value,
                        'transactionType': transaction_type,
                        'latitude': latitude,
                        'longitude': longitude
                    })
                    
                    print(f"   [성공] {transaction_type}, 가격: {price_value}, 월세: {rent_value} 저장 완료.")
                    
                    # --- 상세 페이지 닫기 (주석 처리됨) ---
                    # close_button_selectors = [ ... ]
                    # ...
                    
                    time.sleep(1) 
                
                except Exception as click_error:
                    print(f"   [오류] 매물 {i+1} 처리 실패: {click_error}. 목록으로 복구합니다.")
                    try:
                        driver.refresh() 
                        WebDriverWait(driver, 15).until(EC.presence_of_element_located((By.CSS_SELECTOR, item_selector)))
                        time.sleep(2)
                    except:
                        pass
                    continue
            
            # --- 6. 다음 페이지 버튼 클릭 ---
            next_button_xpath = "//button[@aria-label='다음 페이지' and not(@disabled)]"
            try:
                next_button = driver.find_element(By.XPATH, next_button_xpath)
                driver.execute_script("arguments[0].scrollIntoView({block: 'center'});", next_button)
                driver.execute_script("arguments[0].click();", next_button)
                
                print(f"{dong_name}: 다음 페이지 ({page_num + 1})로 이동.")
                page_num += 1
                time.sleep(5) 
            except NoSuchElementException:
                print(f"{dong_name}: 다음 페이지 버튼이 없어 페이지네이션 종료.")
                break 
            
            # break # 테스트용으로 1페이지만 스크래핑

    except Exception as search_error:
        print(f"{dong_name}: 치명적인 오류 발생: {search_error}")
        continue

# --- 7. 최종 결과 CSV 저장 ---
driver.quit()

if final_scraped_data:
    columns = ['주소', '면적', '가격', '월세', '타입', '위도', '경도']
    df = pd.DataFrame(final_scraped_data, columns=columns)
    
    # 중복 판단 및 제거
    initial_count = len(df)
    
    cols_to_check = ['address', 'area', 'price', 'rent', 'transactionType']
    df = df.drop_duplicates(subset=cols_to_check, keep='first')
    
    removed_count = initial_count - len(df)
    print(f"\n[중복 제거] 총 {initial_count}개 중 {removed_count}개의 중복 데이터를 삭제했습니다.")
    print(f"남은 데이터: {len(df)}개")

    print("\n\n========================================================")
    print(" 최종 스크래핑 결과 (DataFrame 미리보기) ")
    print("========================================================")
    print(df.head(10).to_string(index=False)) 
    
    CSV_FILENAME = 'map_data.csv'
    try:
        current_directory = os.getcwd() 
        df.to_csv(CSV_FILENAME, index=False, encoding='utf-8-sig')
        print(f"\n 최종 데이터가 '{CSV_FILENAME}' 파일로 성공적으로 저장되었습니다.")
        print(f" 저장된 경로: {current_directory}")
    except Exception as e:
        print(f"\n CSV 파일 저장 중 오류 발생: {e}")
        
else:
    print("\n\n 수집된 매물 데이터가 없습니다.")