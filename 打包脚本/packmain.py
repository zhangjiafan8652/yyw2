# -*- coding: utf-8 -*-
import json
import time
from concurrent.futures import ThreadPoolExecutor
import urllib.request
import urllib.parse
from autopack import AutoPack

import requests
import re, os
mytaskcounts = 0
basetaskurl = 'https://cpanel.yayawan.com'
taskurl = 'https://cpanel.yayawan.com/?act=game.pack_tool_api&action=get_task&key=wxj9dlpe&type=2'

packsuceepat='https://cpanel.yayawan.com/?act=game.pack_tool_api&action=report_packer_info&key=wxj9dlpe&app_id=2&packagename=com.yyyyy.cccc&size=100&versioncode=1&versionname=1.0&path=xxx/xxasdasd/xxaax.apk'

# 参数times用来模拟网络请求的时间
tasklist = []

def getTask(type):
    global mytaskcounts
    print(taskurl)
    mytaskcounts = mytaskcounts + 1

    response = urllib.request.urlopen(taskurl)
    gameinfo = json.loads(response .read().decode('utf-8'))
    print(gameinfo)

    au = AutoPack()
    au.setparam(gameinfo['data']['packageName'], gameinfo['data']['targetSdkVersion'],
                gameinfo['data']['sdk'],gameinfo['data']['baseGameUrl'],
                'kk'+gameinfo['data']['gameAppId'], gameinfo['data']['appName'],
                gameinfo['data']['gameIconUrl'],
                json.dumps(gameinfo['data']['metaData']))
    au.before()

    mytaskcounts = mytaskcounts - 1
    #print(gameinfo['data'])
    #print(response .read().decode('utf-8'))


def w_file (filepath, strlog):
    f = open(filepath, 'a+')
    f.write(strlog+'\n')
    f.close()


def jfprint(strlog):
    print(strlog)
    apkLogPath = 'packlog.txt'
    # print(apkLogPath)
    w_file(apkLogPath, strlog)

def getTask1(type):
    global mytaskcounts
    jfprint(taskurl)
    response = requests.get(taskurl)
    gameinfo = response.json()
    #jfprint(response.json())
    #print(gameinfo['data']['gameAppId'])
    print(gameinfo['err_msg'])
    if 'no task' != gameinfo['err_msg']:
        print('1')
        tempack ='kk' + str(gameinfo['data']['gameAppId'])
        print(tempack)
        if tempack in tasklist:
            jfprint('已经在打包'+tempack )
        else:
            au = AutoPack()
            # err_code =1
            jfprint('baseGameUrl:' + gameinfo['data']['baseGameUrl'])
            au.setparam(gameinfo['data']['packageName'], gameinfo['data']['targetSdkVersion'],
                            gameinfo['data']['sdk'],gameinfo['data']['baseGameUrl'],
                             'kk' + str(gameinfo['data']['gameAppId']), gameinfo['data']['appName'],
                            gameinfo['data']['gameIconUrl'],json.dumps(gameinfo['data']['metaData']))
            jfprint('添加打包：' + tempack)
            # 控制只取4个任务
            mytaskcounts = mytaskcounts + 1
            tasklist.append(tempack)
            jfprint("version：3")
            try:
                au.before()
                jfprint('打包成功保存路径：' + au.saveapppath)
                sendTaskresult(au)
                tasklist.remove(tempack)
                jfprint('移除打包：' + tempack)
                mytaskcounts = mytaskcounts - 1
            except Exception as e:
                jfprint('任务异常' + repr(e))
            finally:
                tasklist.remove(tempack)
                jfprint('移除打包：' + tempack)
                mytaskcounts = mytaskcounts - 1

    else:
        print('没有任务')

def sendTaskresult(au):
    jfprint("sendTaskresult：" )
    ku = AutoPack()
    ku = au
    sendurl = 'https://cpanel.yayawan.com/?act=game.pack_tool_api&action=report_packer_info&key=wxj9dlpe'
    sendurl =sendurl + '&app_id=' + str(ku.gameAppId).replace("kk", '')
    gameinfo = getVersiondict(ku.saveapppath)
    sendurl = sendurl +'&packagename=' + gameinfo['packagename'][0]
    sendurl =sendurl + '&versioncode=' + gameinfo['versioncode'][0]
    sendurl = sendurl +'&versionname=' + gameinfo['versionname'][0]
    sendurl = sendurl + '&size=' + str(os.path.getsize(ku.saveapppath))
    #/pub/sharedir/
    tenpath = ku.saveapppath.replace('/pub/sharedir/', '')
    sendurl = sendurl + '&path=' + tenpath
    jfprint("上报成功：" + sendurl)
    responsesucc = requests.get(sendurl)
    gameinfo = responsesucc.json()
    jfprint("上报成功："+str(gameinfo))
    # response = requests.get(taskurl)
    # gameinfo = response.json()

def getVersiondict(apkpath):
    cmd_align = r'aapt d badging  %s ' % (apkpath)
    appinfo = os.system(cmd_align)
    resp = os.popen(cmd_align).readlines()
    print(resp)
    tempstring = str(resp[0])
    spitarr = tempstring.split(" ")
    packagenamestring = spitarr[1]
    packagename = subString2(packagenamestring)

    versioncodestring = spitarr[2]
    versioncode = subString2(versioncodestring)

    versionnamestring = spitarr[3]
    versionname = subString2(versionnamestring)

    dict = {'packagename': packagename, 'versioncode': versioncode, 'versionname': versionname}
    return dict

def subString2(template):
    rule = r'\'(.*?)\''
    slotList = re.findall(rule, template)
    return slotList

if __name__ == "__main__":
    jfprint('循环任务开始')
    executor = ThreadPoolExecutor(max_workers=4)
    while 1:
        try:
            if mytaskcounts < 4:
                getTask1(1)
        except Exception as e:
            jfprint('任务异常'+repr(e))
        else:
            time.sleep(15)
        time.sleep(15)
        jfprint('继续取任务')
    # 通过submit函数提交执行的函数到线程池中，submit函数立即返回，不阻塞
    #task1 = executor.submit(get_html, (3),(1))

# if __name__ == "__main__":
#     getTask1(1)
