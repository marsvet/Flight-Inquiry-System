import time
import datetime
from lxml import etree
from selenium import webdriver
import re
import MySQLdb


print("连接数据库")
conn = MySQLdb.connect(host="localhost", port=3306,
                       db="flight_inquiry", user="root", passwd="root", charset="utf8")


print("启动chrome")
chromeOption = webdriver.ChromeOptions()
chromeOption.add_argument("--headless")
chromeOption.add_argument("--no-sandbox")
driver = webdriver.Chrome(options=chromeOption)


# 读取 thereCode.txt 中的三字码
print("读取三字码")
with open('thereCode.txt', encoding='utf-8') as fp:
    result = fp.readlines()


print("开始循环")
for i in range(3, 11):
    delta = datetime.timedelta(days=i)
    flightDate = (datetime.datetime.now() + delta).strftime('%Y-%m-%d')

    for srcCode in result:
        for dstCode in result:
            if srcCode == dstCode:
                continue

            srcCode = srcCode.strip()
            dstCode = dstCode.strip()

            url = f"https://flights.ctrip.com/itinerary/oneway/{srcCode}-{dstCode}?date={flightDate}"
            print(url)
            driver.get(url)

            print("进入睡眠")
            time.sleep(10)
            print("结束睡眠")

            print("关闭广告")
            # 关闭广告
            try:
                element = driver.find_element_by_xpath(
                    "//*[@id='base_bd']/div[8]/div[2]/div/i")
                driver.find_element_by_class_name("ico-close-b").click()
            except:
                pass
            time.sleep(0.5)
            try:
                element = driver.find_element_by_xpath(
                    '//*[@class="pop_bd"]//a[@class="button"]')
                element.click()
            except:
                pass

            # 滚动页面（自适应高度）
            print("开始滚动页面")
            top = 500
            scrollHeight = driver.execute_script(
                "return document.body.scrollHeight")
            while top < scrollHeight:
                print("当前距网页顶部：" + str(top) + "px")
                driver.execute_script(
                    "document.documentElement.scrollTop = " + str(top))  # 执行 js 代码
                top += 500
                time.sleep(0.7)
                scrollHeight = driver.execute_script(
                    "return document.body.scrollHeight")

            print("获取网页源码")
            pageSource = etree.HTML(driver.page_source)
            flightList = pageSource.xpath(
                '//*[@id="base_bd"]/div[3]/div[1]/div[2]/div/div[2]/div/div/div/div')

            print(flightList)

            print("开始提取信息")
            for item in flightList:
                try:
                    # 航空公司
                    airline = item.xpath(
                        'div[@class="inb logo"]/div[1]/div[1]/span[1]/span[1]/strong[1]/text()')[0]
                    # 航班号
                    flightNum = item.xpath(
                        'div[@class="inb logo"]/div[1]/div[1]/span[1]/span[1]/span[1]/text()')[0]
                    # 机型
                    planeType = item.xpath(
                        'div[@class="inb logo"]/div[2]/span[1]/text()')[0]
                    # 起飞时间
                    startTime = flightDate + " " + item.xpath(
                        'div[@class="inb right"]/div[1]/strong[1]/text()')[0]
                    # 始发地 和 起飞航站楼
                    startPlace = item.xpath(
                        'div[@class="inb right"]/div[2]/text()')[0]
                    sTerminal = re.search(r'T\d$', startPlace)
                    if sTerminal is not None:
                        startStation = startPlace[:-2]
                        sTerminal = sTerminal.group()
                    else:
                        startStation = startPlace
                        sTerminal = None
                    # 到达时间
                    arriveTime = flightDate + " " + item.xpath(
                        'div[@class="inb left"]/div[1]/strong[1]/text()')[0]
                    # 目的地 和 目的航站楼
                    destPlace = item.xpath(
                        'div[@class="inb left"]/div[2]/text()')[0]
                    dTerminal = re.search(r'T\d$', destPlace)
                    if dTerminal is not None:
                        destStation = destPlace[:-2]
                        dTerminal = dTerminal.group()
                    else:
                        destStation = destPlace
                        dTerminal = None

                    # 入库
                    print("开始入库")
                    print("创建数据库指针")
                    cs = conn.cursor()

                    # x = cs.execute(
                    #     "SELECT * FROM airline WHERE zh_full_name=%s",
                    #     [airline]
                    # )
                    # if x == 0:
                    #     cs.execute(
                    #         "INSERT INTO airline VALUES(UUID(), null, null, 1, null, null, null, %s, null)",
                    #         [airline]
                    #     )
                    #     conn.commit()
                    # cs.execute(
                    #     "SELECT * FROM airline WHERE zh_full_name=%s",
                    #     [airline]
                    # )

                    # airlineId = cs.fetchone()[0]

                    print("执行插入语句")
                    cs.execute(
                        "INSERT INTO flight_info VALUES(UUID(), %s, %s, %s, %s, %s, %s, %s, %s, %s, 1)",
                        [airline, arriveTime, destStation, dTerminal, flightNum,
                            planeType, startStation, sTerminal, startTime]
                    )
                    print("提交")
                    conn.commit()

                    print("------------------------------------------------------")
                    print(airline, arriveTime, destStation, dTerminal, flightNum,
                          planeType, startStation, sTerminal, startTime)
                    print("------------------------------------------------------")

                except Exception as e:
                    print(e)
                    pass
