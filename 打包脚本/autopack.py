# -*- coding: utf-8 -*-
import re, os, shutil, zipfile, sys, xml.dom.minidom, xml.etree.ElementTree, subprocess, urllib.request
from imp import reload
from xml.dom.minidom import parse

reload(sys)
# sys.setdefaultencoding("utf8")


'''
1 下载游戏包到  basepackingpath
2 根据游戏名称创建打包文件夹
3 整合参数
4 反编译游戏包
5 整合参数
6 正编译
'''


class AutoPack:
    # basePackingPath = '/mnt/e/linuxwenjian/packingapp/share'
    aliyunPath = '/pub/sharedir/zpackapk'
    basePackingPath = '/pub/qianqipack'
    apkToolPath = basePackingPath + '/tool'
    apkLogPath = ''
    keystore = basePackingPath + '/tool/yayawan.keystore'
    keystorebieming = 'wjy'
    keystorepassword = '43597441'
    singleworkingdir = ''
    packageName = ''
    targetSdkVersion = ''
    sdk = ''
    baseGameUrl = ''
    gameAppId = ''

    appName = ''

    gameIconUrl = ''
    metaData = ''
    sdkname = ''
    saveapppath = ''

    def setparam(self, packagename, mtargetsdkversion, msdk, mbasegameurl, mgameappid, mappname,
                 mgameiconurl, mmetadata):

        self.packageName = packagename
        self.jfprint("self.packageName：" + self.packageName)
        self.targetSdkVersion = mtargetsdkversion
        self.sdk = msdk
        self.jfprint("self.sdk ：" + self.sdk )
        self.sdkname = msdk
        self.baseGameUrl = mbasegameurl
        self.jfprint("self.baseGameUrl ：" + self.baseGameUrl)
        self.gameAppId = mgameappid
        self.jfprint("self.gameAppId ：" + self.gameAppId)
        self.appName = mappname

        self.gameIconUrl = mgameiconurl
        self.metaData = mmetadata
        self.singleworkingdir = self.basePackingPath + '/' + mgameappid + mappname + '/'
        self.apkLogPath = self.singleworkingdir + 'log.txt'

    '''
    do something before packing
    '''

    def before(self):

        if os.path.exists(self.singleworkingdir):
            self.jfprint("文件已经存在：" + self.singleworkingdir)
        else:
            os.makedirs(self.singleworkingdir)
            self.jfprint('创建文件夹成功：' + self.singleworkingdir)
        self.jfprint(self.singleworkingdir)
        self.jfprint(self.singleworkingdir + "+" + self.baseGameUrl + "===" + self.appName)
        self.downloadgame(self.baseGameUrl, self.appName, self.singleworkingdir)
        self.jfprint('开始把渠道sdk 的yayawan中间件更新')
        game_path = self.singleworkingdir + self.appName + '.apk'
        sdk_path = self.basePackingPath + '/sdks/' + self.sdk + '.zip'
        if os.path.exists(self.singleworkingdir + 'game/'):
            shutil.rmtree(self.singleworkingdir + 'game/')
        if not self.unzip(sdk_path, self.singleworkingdir + 'game/', False):
            self.jfprint('unzip sdk is failed')
        if not self.unzip(self.basePackingPath + '/sdks/SDKyyw.zip', self.singleworkingdir + 'yyw/', False):
            self.jfprint('unzip SDKyyw.zip is failed')
        else:
            tmpsdkpath = self.singleworkingdir + 'game/' + "smali/com/yayawan/"
            for p in os.listdir(tmpsdkpath):
                if p != 'impl' and p != 'sdktemplate':
                    shutil.rmtree(tmpsdkpath + p)
            shutil.rmtree(self.singleworkingdir + 'yyw/smali/com/yayawan/impl')

            if os.path.exists(self.singleworkingdir + 'yyw/smali/com/yayawan/impl'):
                shutil.rmtree(self.singleworkingdir + 'yyw/smali/com/yayawan/impl')
            if os.path.exists(self.singleworkingdir + 'yyw/smali/android'):
                shutil.rmtree(self.singleworkingdir + 'yyw/smali/android')
            os.remove(self.singleworkingdir + 'yyw/AndroidManifest.xml')
            os.system(r'cp -r %s %s' % (self.singleworkingdir + 'game/*', self.singleworkingdir + 'yyw/'))
            shutil.rmtree(self.singleworkingdir + 'game')
            os.rename(self.singleworkingdir + 'yyw', self.singleworkingdir + 'game')
        self.jfprint('把渠道sdk 的yayawan中间件部分更新成功')
        self.jfprint('开始下载图片')
        iconsavefileurl = self.downloadgameicon(self.gameIconUrl, self.appName, self.singleworkingdir)
        self.createiconzipfile(iconsavefileurl, self.singleworkingdir)
        self.jfprint('开始拆包')
        self.packing(game_path, self.apkToolPath)

    def packing(self, gamepath, apktooldir):
        self.jfprint('packing.....')
        pathname = gamepath
        filename = os.path.basename(pathname)
        easyName, _ = os.path.splitext(filename)
        logstr = "\n" + 'Start packing ----- ' + filename + ' ----- ' + "\n"
        apktoolpath = apktooldir + '/apktool.jar'
        cmd = r'java -jar %s d -f %s -o %stemp' % (apktoolpath, pathname, self.singleworkingdir)
        self.jfprint('执行拆包命令：' + cmd)
        os.system(cmd)
        self.jfprint('替换图片：' + cmd)
        self.rename_icon(gamepath, self.singleworkingdir + 'icon/res/')
        self.jfprint('easyName' + easyName + 'filename' + filename)
        self.channel(easyName, filename)

    '''
    change XML file and build apk
    '''
    _oldpackagename = ''

    def channel(self, code, filename):
        easyname, _ = os.path.splitext(filename)

        '''
        if has folder, copy this to temp
        '''
        if os.path.exists(self.singleworkingdir + 'game'):

            if os.path.exists(self.singleworkingdir + 'game' + '/AndroidManifest.xml'):
                self.jfprint('bengin merger xml')
                # self.basePackingPath + '/' + easyname + '/AndroidManifest.xml' 这个路径是sdk 的
                # self.jfprint(self.singleworkingdir + 'game' + '/AndroidManifest.xml', self.singleworkingdir + 'temp/AndroidManifest.xml')
                yyw = xml.dom.minidom.parse(self.singleworkingdir + 'game' + '/AndroidManifest.xml')
                old = xml.etree.ElementTree.parse(self.singleworkingdir + 'temp/AndroidManifest.xml')
                self.jfprint('创建uploadfile')
                self.createuploadsdkfile()
                custom = xml.dom.minidom.parse(self.singleworkingdir + 'uploadsdk.xml')
                yywroot = yyw.documentElement
                oldxmltemp = xml.dom.minidom.parse(self.singleworkingdir + 'temp/AndroidManifest.xml')
                oldxmltempel = oldxmltemp.documentElement
                self.jfprint("oldxmltempelpackage: %s" % oldxmltempel.getAttribute("package"))
                _oldpackagename = oldxmltempel.getAttribute("package")
                oldroot = old.getroot()
                customroot = custom.documentElement
                self.mergerxml(yywroot, oldroot)
                self.jfprint('merger AndroidManifest.xml is ok')
                self.mergerxml(customroot, oldroot)
                self.jfprint('merger uploadsdk.xml is ok')

                package = customroot.attributes.items()[1][1]
                if package != '':
                    oldroot.set('package', package)

                self.indent(oldroot)

                xml.etree.ElementTree.register_namespace('android', 'http://schemas.android.com/apk/res/android')
                old.write(self.singleworkingdir + 'temp/AndroidManifest.xml', 'utf-8')
                self.jfprint('update ~/temp/AndroidManifest.xml is ok')
                os.remove(self.singleworkingdir + 'game' + '/AndroidManifest.xml')
                self.jfprint('remove AndroidManifest.xml is ok')

                # tag1

            x2 = xml.etree.ElementTree.parse(self.singleworkingdir + 'temp/AndroidManifest.xml').getroot()
            c2 = xml.etree.ElementTree.parse(self.singleworkingdir + 'uploadsdk.xml')

            _appid = ""
            _appwxkey = ""
            _appwxid = ""
            _oldappname = ""
            _newappname = ""
            for elem in c2.iter(tag='meta-data'):
                print(elem.tag, elem.attrib)
                self.jfprint(elem.attrib["{http://schemas.android.com/apk/res/android}name"])
                if elem.attrib["{http://schemas.android.com/apk/res/android}name"] == "appid":
                    _appid = elem.attrib["{http://schemas.android.com/apk/res/android}value"]

                if elem.attrib["{http://schemas.android.com/apk/res/android}name"] == "wxAppKey":
                    _appwxkey = elem.attrib["{http://schemas.android.com/apk/res/android}value"]

                if elem.attrib["{http://schemas.android.com/apk/res/android}name"] == "wxAppId":
                    _appwxid = elem.attrib["{http://schemas.android.com/apk/res/android}value"]

                if elem.attrib["{http://schemas.android.com/apk/res/android}name"] == "oldappname":
                    _oldappname = elem.attrib["{http://schemas.android.com/apk/res/android}value"]
                    _oldappname = _oldappname.decode('utf-8', 'ignore')

                if elem.attrib["{http://schemas.android.com/apk/res/android}name"] == "newappname":
                    _newappname = elem.attrib["{http://schemas.android.com/apk/res/android}value"]
                    # _newappname = _newappname.decode('utf-8', 'ignore')
            _newappname = self.appName
            self.jfprint('选择签名文件')
            if elem.attrib["{http://schemas.android.com/apk/res/android}name"] == "signfilename":
                self._signfilename = elem.attrib["{http://schemas.android.com/apk/res/android}value"]
                if self._signfilename == "":
                    self._signfilename = "keystore.conf"
            self.jfprint('获取包名---')

            _package_name = self.packageName
            self.jfprint('设置联想参数')
            if self.sdkname == "SdkLenovo":
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'android.intent.action.MAIN',
                                'lenovoid.MAIN')
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'android.intent.category.LAUNCHER',
                                'android.intent.category.DEFAULT')
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_action',
                                'android.intent.action.MAIN')
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_category',
                                'android.intent.category.LAUNCHER')
            if self.sdkname == "SdkLenovo_hengpin":
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'android.intent.action.MAIN',
                                'lenovoid.MAIN')
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'android.intent.category.LAUNCHER',
                                'android.intent.category.DEFAULT')
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_action',
                                'android.intent.action.MAIN')
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_category',
                                'android.intent.category.LAUNCHER')
            self.jfprint('更改app名字--------------------')
            self.renamexml(self.singleworkingdir + 'temp', _newappname)
            self.jfprint('SDKyywlogin-----------修改')

            if self.sdkname == "SDKyywlogin" and _oldappname != "no":
                self.xmlreplacejiafan(self.singleworkingdir + 'temp/res/values/strings.xml', _oldappname, _newappname)

            if self.sdkname != "Sdkali":
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'android.permission.SEND_SMS',
                                'android.permission.WRITE_EXTERNAL_STORAGE')
            self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string-package', _package_name)
            self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', _oldpackagename, _package_name)
            self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string-package', _package_name)
            self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string-appid', _appid)
            self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string-app_name', '@string/app_name')
            self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_package', _package_name)
            self.repackagenamexml(self.singleworkingdir + 'temp/AndroidManifest.xml', _package_name)
            self.jfprint('修改privader的packagename')
            self.reProviderAuthorityToPackagename(self.singleworkingdir + 'temp/AndroidManifest.xml', _package_name)
            self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_appid', _appid)

            self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_app_name', '@string/app_name')
            # self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'com.alipay.sdk.app.H5PayActivity', 'tempActivity')
            # self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'com.alipay.sdk.auth', 'tempActivity')

            if self.sdkname == "SdkQQY":
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_appwxid', _appwxid)
            if self.sdkname == "SdkQQD":
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_appwxid', _appwxid)
            if self.sdkname == "SdkQQM":
                self.xmlreplace(self.singleworkingdir + 'temp/AndroidManifest.xml', 'string_appwxid', _appwxid)

            # /pub/tool/XmlMerge.jar
            mergercmd = r'/usr/bin/java -jar %s %s %s  > /dev/null' % (
                self.apkToolPath + '/XmlMerge.jar', self.singleworkingdir + 'game', self.singleworkingdir + '/temp')
            # os.system(mergercmd)
            # fs = os.popen(mergercmd,  'r')
            # x = fs.read()
            proc = subprocess.Popen(mergercmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
            (out, err) = proc.communicate()
            self.jfprint('mergercmd--- %s ----\nis good' % (mergercmd))

            fd = open(self.singleworkingdir + "snmptest", "w")
            strout = str(out, encoding="utf-8")
            errstr = str(err, encoding="utf-8")
            fd.write(strout)
            fd.write(errstr)
            fd.close()

            if (os.path.exists(self.singleworkingdir + 'temp/smali/' + _package_name.replace('.', '/'))):
                self.jfprint('cunzai %s ' % (_package_name.replace('.', '/')))

            if os.path.exists(self.singleworkingdir + 'game/smali/com/yayawan/sdktemplate'):
                if (os.path.exists(self.singleworkingdir + 'temp/smali/' + _package_name.replace('.', '/'))):
                    self.jfprint('bu chuangjian %s ' % (_package_name.replace('.', '/')))
                else:
                    self.jfprint('chuangjian %s ' % (_package_name.replace('.', '/')))
                    os.system(r'mkdir -p ' + self.singleworkingdir + 'temp/smali/' + _package_name.replace('.', '/'))
                os.system(r'cp -r %s %s' % (self.singleworkingdir + 'game/smali/com/yayawan/sdktemplate/*',
                                            self.singleworkingdir + 'temp/smali/' + _package_name.replace('.', '/')))
                shutil.rmtree(self.singleworkingdir + 'game/smali/com/yayawan/sdktemplate')
                self.jfprint('copy folder sdktemplate is ok  -- packagename -- ' + _package_name)

                # replace
                sdktemplate_dir = self.singleworkingdir + 'temp/smali/' + _package_name.replace('.', '/')

                listfile = self.GetFileList(sdktemplate_dir, [])
                for smali_file1 in listfile:
                    self.jfprint('jiafan zhaodaode wenjian smali_file3++++' + smali_file1)

                    if os.path.isfile(smali_file1):
                        save_smali_data = re.sub(r'com/yayawan/sdktemplate', _package_name.replace('.', '/'),
                                                 open(smali_file1).read())
                        open(smali_file1, 'w').write(save_smali_data)

            #  如果是qqysdk则需要向assets中写入文件
            if self.sdkname == "SdkQQY":
                fh = open(self.basePackingPath + '/game/assets/ysdkconf.ini', 'w')
                fh.write('QQ_APP_ID=' + _appid.replace('string',
                                                       '') + '\r\n' + 'WX_APP_ID=' + _appwxid + '\r\n' + 'OFFER_ID=' + _appid.replace(
                    'string', '') + '\r\n' + 'YSDK_URL=https://ysdk.qq.com')
                fh.close()

            if self.sdkname == "SdkQQD":
                fh = open(self.basePackingPath + '/game/assets/ysdkconf.ini', 'w')
                fh.write('QQ_APP_ID=' + _appid.replace('string',
                                                       '') + '\r\n' + 'WX_APP_ID=' + _appwxid + '\r\n' + 'OFFER_ID=' + _appid.replace(
                    'string', '') + '\r\n' + 'YSDK_URL=https://ysdk.qq.com')
                fh.close()
                self.jfprint('+++++++++++写入sdkqqy配置文件+++++')

            if self.sdkname == "SdkQQM":
                fh = open(self.basePackingPath + '/game/assets/ysdkconf.ini', 'w')
                fh.write('QQ_APP_ID=' + _appid.replace('string',
                                                       '') + '\r\n' + 'WX_APP_ID=' + _appwxid + '\r\n' + 'OFFER_ID=' + _appid.replace(
                    'string', '') + '\r\n' + 'YSDK_URL=https://ysdk.qq.com')
                fh.close()
                self.jfprint('+++++++++++写入sdkqqM配置文件+++++')

            if self.sdkname == "SdkTTyuyin":
                fh = open(self.basePackingPath + '/game/assets/TTGameSDKConfig.cfg', 'w')
                fh.write(_appwxid)
                fh.close()
                self.jfprint('+++++++++++写入sdkttyuying配置文件+++++')



            else:
                self.jfprint('sdktemplate folder  is not -- ' + _package_name)

            if os.path.exists(self.singleworkingdir + 'game' + '/res/values'):
                shutil.rmtree(self.singleworkingdir + 'game' + '/res/values')
            self.jfprint('remove value folder is good')

            game_armeabi = False
            game_armeabi_v7a = False

            sdk_armeabi = False
            sdk_armeabi_v7a = False
            sdk_x86 = False
            game_x86 = False

            if os.path.exists(self.singleworkingdir + 'temp/lib/armeabi'):
                game_armeabi = True
            if os.path.exists(self.singleworkingdir + 'temp/lib/armeabi-v7a'):
                game_armeabi_v7a = True
            if os.path.exists(self.singleworkingdir + 'temp/lib/x86'):
                game_x86 = True

            if os.path.exists(self.singleworkingdir + 'game' + '/lib/armeabi'):
                sdk_armeabi = True
            if os.path.exists(self.singleworkingdir + 'game' + '/lib/armeabi-v7a'):
                sdk_armeabi_v7a = True
            if os.path.exists(self.singleworkingdir + 'game' + '/lib/x86'):
                sdk_x86 = True

            if sdk_armeabi == True and sdk_armeabi_v7a == False:
                os.system(r'cp -r %s %s' % (self.singleworkingdir + 'game' + '/lib/armeabi',
                                            self.singleworkingdir + 'game' + '/lib/armeabi-v7a'))
                sdk_armeabi_v7a = True
                self.jfprint('copy sdk_armeabi_v7a and update folder is ok')

            if sdk_armeabi == False and sdk_armeabi_v7a == True:
                os.system(r'cp -r %s %s' % (self.singleworkingdir + 'game' + '/lib/armeabi-v7a',
                                            self.singleworkingdir + 'game' + '/lib/armeabi'))
                sdk_armeabi = True
                self.jfprint('copy sdk_armeabi and update folder is ok')

            if sdk_x86 == False and game_x86 == True and sdk_armeabi_v7a == True:
                os.system(r'cp -r %s %s' % (self.singleworkingdir + 'game' + '/lib/armeabi-v7a',
                                            self.singleworkingdir + 'game' + '/lib/x86'))
                # sdk_armeabi= True


            elif sdk_x86 == False and game_x86 == True and sdk_armeabi == True:
                os.system(r'cp -r %s %s' % (self.singleworkingdir + 'game' + '/lib/armeabi',
                                            self.singleworkingdir + 'game' + '/lib/x86'))
                self.jfprint('copy sdk_x86 and update folder is ok')

            if game_armeabi == False and sdk_armeabi == True:
                shutil.rmtree(self.singleworkingdir + 'game' + '/lib/armeabi')
            if game_x86 == False and sdk_x86 == True:
                shutil.rmtree(self.singleworkingdir + 'game' + '/lib/x86')
                self.jfprint('dele sdk_x86 x86 and update folder is ok')

            if game_armeabi_v7a == False and sdk_armeabi_v7a == True:
                shutil.rmtree(self.singleworkingdir + 'game' + '/lib/armeabi-v7a')

            ###small2 class
            if os.path.exists(self.singleworkingdir + 'temp/smali_classes2'):

                if os.path.exists(self.singleworkingdir + 'temp/smali_classes2/com/yayawan'):
                    shutil.copy(self.singleworkingdir + 'game/smali/com/yayawan',
                                self.singleworkingdir + "game/smali_classes2/com/yayawan")

                if not os.path.exists(self.singleworkingdir + 'temp/smali/com/yayawan'):
                    shutil.rmtree(self.singleworkingdir + 'game/smali/com/yayawan')

                if os.path.exists(self.singleworkingdir + 'temp/smali_classes2/com/alipay'):
                    shutil.copy(self.singleworkingdir + 'game/smali/com/alipay',
                                self.singleworkingdir + "game/smali_classes2/com/alipay")
                if os.path.exists(self.singleworkingdir + 'temp/smali_classes2/com/lidroid'):
                    shutil.copy(self.singleworkingdir + 'game/smali/com/lidroid',
                                self.singleworkingdir + "game/smali_classes2/com/lidroid")

                if not os.path.exists(self.singleworkingdir + 'temp/smali/com/alipay'):
                    shutil.rmtree(self.singleworkingdir + 'game/smali/com/alipay')
                if not os.path.exists(self.singleworkingdir + 'temp/smali/com/lidroid'):
                    shutil.rmtree(self.singleworkingdir + 'game/smali/com/lidroid')

            smali_str = 'smali'

            if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan'):
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan')
            if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/alipay'):
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/alipay')
            if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/lidroid'):
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/lidroid')

            if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/ipaynow'):
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/ipaynow')
            if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/switfpass'):
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/switfpass')
            if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/ta'):
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/ta')
            if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/ut'):
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/ut')
            if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/xqt'):
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/xqt')

            if os.path.exists(self.singleworkingdir + 'temp/smali_classes2'):
                smali_str = 'smali_classes2'
                if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan'):
                    shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan')
                if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/alipay'):
                    shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/alipay')
                if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/lidroid'):
                    shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/lidroid')
                if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/ipaynow'):
                    shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/ipaynow')
                if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/switfpass'):
                    shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/switfpass')
                if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/ta'):
                    shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/ta')
                if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/ut'):
                    shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/ut')
                if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/xqt'):
                    shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/xqt')

            os.system(r'cp -r %s %s' % (self.singleworkingdir + 'game' + '/*', self.singleworkingdir + 'temp/'))
            self.jfprint('copy and update smali_classes2 folder is ok')

            if os.path.exists(self.basePackingPath + '/icon'):
                os.system(r'cp -r %s %s' % (self.singleworkingdir + 'icon/*', self.singleworkingdir + 'temp/'))
                self.jfprint('copy icon to temp is ok')

        if self.sdkname == "SdkUCjiuyou":
            smali_str = 'smali'
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/jflib')
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/jflogin')
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/jfpay')
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/jfsmallhelp')
            if os.path.exists(self.singleworkingdir + 'temp/smali_classes2'):
                smali_str = 'smali_classes2'
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/jflib')
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/jflogin')
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/jfpay')
                shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/jfsmallhelp')
        if self.sdkname == "Sdkali":
            smali_str = 'smali'
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/pay')
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/xml')
            shutil.rmtree(self.singleworkingdir + 'temp/' + 'assets' + '/yayaassets')
        if self.sdkname == "Sdkaxiaomi":
            smali_str = 'smali'
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/pay')
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/xml')
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/implyy')
            shutil.rmtree(self.singleworkingdir + 'temp/' + 'assets' + '/yayaassets')
        if self.sdkname == "Sdkvivo":
            smali_str = 'smali'
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/pay')
            shutil.rmtree(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/sdk/xml')
            shutil.rmtree(self.singleworkingdir + 'temp/' + 'assets' + '/yayaassets')
            if os.path.exists(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/proxy/GameApi.smali'):
                os.remove(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/proxy/GameApi.smali')
                os.remove(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/proxy/GameApi$1.smali')
                os.remove(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/proxy/GameApi$2.smali')
                os.remove(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/proxy/GameApi$3.smali')
                os.remove(self.singleworkingdir + 'temp/' + smali_str + '/com/yayawan/proxy/GameApi$4.smali')

        if os.path.exists(self.singleworkingdir + '/package_temp/' + self.packageName + '/game.apk'):
            os.remove(self.singleworkingdir + 'package_temp/' + self.packageName + '/game.apk')

        easyName = self.packageName
        unsignApk = r'%spackage_temp/%s/%s_unsigned.apk' % (self.singleworkingdir, easyName, code)
        # tag2

        self.xmlreplacegamename(self.singleworkingdir + 'temp/AndroidManifest.xml', 'android:value="@xml',
                                'android:resource="@xml')
        f = open(self.singleworkingdir + 'temp/AndroidManifest.xml', 'r')
        tempXML = f.read()
        f.close()
        self.jfprint('start b apk:' + tempXML)

        cmdPack = r'java -jar %s/apktool.jar b %stemp -o %s' % (self.apkToolPath, self.singleworkingdir, unsignApk)
        self.jfprint(cmdPack)

        os.system(cmdPack)

        # self.getKeystore()

        unsignedjar = r'%spackage_temp/%s/%s_unsigned.apk' % (self.singleworkingdir, easyName, code)
        signed_unalignedjar = r'%spackage_temp/%s/%s_signed_unaligned.apk' % (self.singleworkingdir, easyName, code)

        succedpath = r'%ssucced' % (self.singleworkingdir)
        self.mkdirj(succedpath)
        signed_alignedjar = r'%ssucced/%s.apk' % (self.singleworkingdir, code)

        save_signed_alignedjar = r'%spackage/%s/%s.apk' % (self.singleworkingdir, easyName, code)

        save_signed_alignedjar_dirname = os.path.dirname(save_signed_alignedjar)

        save_ssh_dirname = r'%spackage/%s/' % (self.singleworkingdir, easyName)

        '''
        add this arg ,if jdk version is too high
        -digestalg SHA1 -sigalg MD5withRSA
        '''

        cmd_sign = r'jarsigner -verbose -digestalg SHA1 -sigalg MD5withRSA -keystore %s -storepass %s -signedjar %s %s %s > /dev/null' % (
            self.keystore, self.keystorepassword, signed_unalignedjar, unsignedjar, self.keystorebieming)
        # cmd_align = r'%s/android-sdk-linux/tools/zipalign -v 4 %s %s > /dev/null' % (self.apkToolPath, signed_unalignedjar, signed_alignedjar)

        cmd_align = r'zipalign -v 4 %s %s > /dev/null' % (signed_unalignedjar, signed_alignedjar)

        os.system(cmd_sign)
        # os.remove(unsignedjar)
        self.jfprint('apk打包命令：' + cmd_sign)
        self.jfprint('apk优化命令：' + cmd_align)
        os.system(cmd_align)
        self.jfprint('开始移除文件：game package_temp temdir temp' )
        shutil.rmtree(self.singleworkingdir + 'game')
        self.jfprint('开始移除文件：package_temp ')
        shutil.rmtree(self.singleworkingdir + 'package_temp')
        self.jfprint('开始移除文件：temp')
        shutil.rmtree(self.singleworkingdir + 'temdir')

        shutil.rmtree(self.singleworkingdir + 'temp')

        os.remove(self.singleworkingdir + 'icon.zip')
        os.remove(self.singleworkingdir + 'snmptest')
        os.remove(self.singleworkingdir + self.appName + '.apk')
        os.remove(self.singleworkingdir + self.appName + '.png')
        os.remove(self.singleworkingdir + 'uploadsdk.xml')
        ## 拷贝到共享文件夹
        self.jfprint('拷贝到共享文件夹：')
        cptofilename =  self.gameAppId + '.apk'
        cptofiledir = self.aliyunPath + '/' + self.gameAppId
        cptofilepath =cptofiledir + '/' +cptofilename
        self.saveapppath = cptofilepath
        if os.path.exists(cptofiledir):
            self.jfprint('拷贝到共享文件：'+cptofilepath)
            os.system(r'cp -r %s %s' % (
                signed_alignedjar, cptofilepath))
        else:
            self.jfprint('拷贝到共享文件：'+cptofilepath)
            os.system(r'mkdir ' + cptofiledir)
            os.system(r'chmod -R 777 ' + cptofiledir)
            os.system(r'cp -r %s %s' % (
                signed_alignedjar, cptofilepath))
        # self.saveapppath = succedpath
        self.jfprint('打包成功dabaochenggong，很棒棒哦~！文件位置：' + succedpath)

        # os.remove(signed_unalignedjar)

        # if os.path.exists(save_signed_alignedjar):
        # shutil.rmtree(save_signed_alignedjar_dirname)

        # os.system(r'yes | cp -fr  %s %s' % (signed_alignedjar, save_signed_alignedjar))

        # shutil.rmtree(os.path.dirname(signed_alignedjar))

    def GetFileList(self, dir, fileList):
        newDir = dir
        if os.path.isfile(dir):
            fileList.append(dir)
        elif os.path.isdir(dir):
            for s in os.listdir(dir):
                newDir = os.path.join(dir, s)
                self.jfprint('+++++++++++zailimian')
                self.GetFileList(newDir, fileList)
        return fileList

    def xmlreplace(self, xml_path, str1, str2):
        tempXML = ''
        f = open(xml_path, 'r')
        tempXML = f.read()
        f.close()
        strinfo = re.compile(str1)
        tempXML = strinfo.sub(str2, tempXML)
        output = open(xml_path, 'w')
        output.write(tempXML)
        output.close()

    def renamexml(self, xmlpath, newappname):
        if os.path.exists(xmlpath):
            oldappname = ''
            stringpath = os.path.join(xmlpath, "res", "values", "strings.xml")

            stringpathcn = os.path.join(xmlpath, "res", "values-zh-rCN", "strings.xml")
            stringpathhk = os.path.join(xmlpath, "res", "values-zh-rHK", "strings.xml")
            stringpathtw = os.path.join(xmlpath, "res", "values-zh-rTW", "strings.xml")
            # AndroidManifest.xml
            stringpathManifest = os.path.join(xmlpath, "AndroidManifest.xml")

            import xml.etree.ElementTree as ET
            tree = ET.parse(stringpath)
            root = tree.getroot()

            for child in root:

                # print('child-tag是：',child.tag,',child.attrib：',child.attrib,',child.text：',child.text)
                # printchild.attrib['name']
                if child.attrib['name'] == "app_name":
                    oldappname = child.text
            if oldappname != '' and newappname != '':
                self.jfprint('准备更改名字 旧名字：' + oldappname + "新名字：" + newappname)
                self.xmlreplacegamename(stringpath, oldappname, newappname)
                self.xmlreplacegamename(stringpathcn, oldappname, newappname)
                self.xmlreplacegamename(stringpathhk, oldappname, newappname)
                self.xmlreplacegamename(stringpathtw, oldappname, newappname)
                self.xmlreplacegamename(stringpathManifest, oldappname, '@string/app_name')


        else:
            self.jfprint('文件不存在')

    def repackagenamexml(self, afxmlpath, newpackagename):
        if os.path.exists(afxmlpath):
            oldpackagename = ''
            import xml.etree.ElementTree as ET
            tree = ET.parse(afxmlpath)
            root = tree.getroot()
            oldpackagename = root.attrib['package']
            if oldpackagename != '' and newpackagename != '':
                self.xmlreplacegamename(afxmlpath, oldpackagename, newpackagename)
                self.jfprint('repalce oldpackage:' + oldpackagename + ' to newpackagename:' + newpackagename)

        else:
            self.jfprint('文件不存在')

    def reProviderAuthorityToPackagename(self, afxmlpath, newpackagename):
        import xml.etree.ElementTree as ET
        print('开始解析')
        tree = ET.parse(afxmlpath)
        root = tree.getroot()
        for child in root.iter():
            # print("Tag:", child.tag, "Attributes:", child.attrib)
            if child.tag == 'application':
                # print("进来了application")
                for applicationchild in child.iter():
                    # print("applicationchildTag:", applicationchild.tag, "Attributes:", applicationchild.attrib)
                    if applicationchild.tag == 'provider':
                        print(applicationchild.attrib['{http://schemas.android.com/apk/res/android}authorities'])
                        if applicationchild.attrib['{http://schemas.android.com/apk/res/android}authorities'] == 'com.yayawan.sdk.other.DgameFileProvider':
                            applicationchild.attrib['{http://schemas.android.com/apk/res/android}authorities'] = newpackagename + ':yyw'
        ET.register_namespace('android', 'http://schemas.android.com/apk/res/android')
        tree.write(afxmlpath, 'utf-8')

    @staticmethod
    def xmlreplacegamename(xml_path, str1, str2):
        tempXML = ''
        if os.path.exists(xml_path):
            f = open(xml_path, 'r')
            tempXML = f.read()
            f.close()
            strinfo = re.compile(str1)
            tempXML = strinfo.sub(str2, tempXML)
            output = open(xml_path, 'w')
            output.write(tempXML)
            output.close()
        else:
            print('文件不存在')

    def indent(self, elem, level=0):
        i = "\n" + level * "  "
        if len(elem):
            if not elem.text or not elem.text.strip():
                elem.text = i + "  "
            for e in elem:
                self.indent(e, level + 1)
            if not e.tail or not e.tail.strip():
                e.tail = i
        if level and (not elem.tail or not elem.tail.strip()):
            elem.tail = i
        return elem

    def mergerxml(self, node, node1):
        for i in node.childNodes:
            if i.nodeName == '#text' or i.nodeName == '#comment':
                continue
            if i.nodeName == 'application':
                tmp = node1.find(i.nodeName)
            elif i.nodeName == 'activity':
                dict = {}
                for val in i.attributes.items():
                    dict[val[0]] = val[1]
                tmp = xml.etree.ElementTree.Element(i.nodeName, dict)
                # if not i.childNodes:
                node1.append(tmp)
            elif i.nodeName == 'uses-permission' or i.nodeName == 'permission':
                has = False
                for v in node1.findall(i.nodeName):
                    for vv in v.attrib:
                        if v.attrib[vv] == i.attributes.items()[0][1]:
                            has = True
                if not has:
                    tmp = xml.etree.ElementTree.Element(i.nodeName, {
                        '{http://schemas.android.com/apk/res/android}name': i.attributes.items()[0][1]})
                    node1.append(tmp)

            elif i.nodeName == 'meta-data':
                has = False
                for v in node1.findall(i.nodeName):
                    for vv in v.attrib:
                        if v.attrib[vv] == i.attributes.items()[0][1]:
                            has = v
                if has == False:
                    tmp = xml.etree.ElementTree.Element(i.nodeName, {
                        '{http://schemas.android.com/apk/res/android}name': i.attributes.items()[0][1],
                        '{http://schemas.android.com/apk/res/android}value': i.attributes.items()[1][1]})
                    node1.append(tmp)
                else:
                    has.set('{http://schemas.android.com/apk/res/android}value', i.attributes.items()[1][1])

            else:
                dict = {}
                for val in i.attributes.items():
                    dict[val[0]] = val[1]
                tmp = xml.etree.ElementTree.Element(i.nodeName, dict)
                node1.append(tmp)

            if i.childNodes:
                self.mergerxml(i, tmp)

    def createuploadsdkfile(self):
        # 1.拷贝基础uploadsdk.xml文件
        self.jfprint('开始写入uploadxml')
        os.system(r'cp -r %s %s' % (self.apkToolPath + '/uploadsdk.xml',
                                    self.singleworkingdir + 'uploadsdk.xml'))
        uploadxmlfilepath = self.singleworkingdir + 'uploadsdk.xml'
        domtree = parse(uploadxmlfilepath)
        self.jfprint(domtree.toxml())
        basedoc = domtree.documentElement
        applicationtag = basedoc.getElementsByTagName('application')[0]
        # 创建一个根节点companys对象
        doc = xml.dom.minidom.Document()
        # 给根节点添加属性
        # print('metadata:' + self.metaData)
        metadatadic = eval(self.metaData)
        for key, value in metadatadic.items():
            metadata = doc.createElement('meta-data')
            metadata.setAttribute('android:name', key)
            metadata.setAttribute('android:value', value)
            applicationtag.appendChild(metadata)
        # print(basedoc.toxml())
        fp = open(self.singleworkingdir + 'uploadsdk.xml', 'w')
        # 将内存中的xml写入到文件
        domtree.writexml(fp, indent='', addindent='\t', newl='\n')
        fp.close()
        self.jfprint('写入uploadxml结束' + self.singleworkingdir + 'uploadsdk.xml')

    def rename_icon(self, game_path, icon_path):
        self.jfprint("game_path" + game_path)
        self.jfprint("icon_path" + icon_path)
        output = os.popen('aapt d badging ' + game_path)
        res = output.read()
        m = re.findall(r"application: label='.*' icon='.*/(.*)'", res)
        self.jfprint("m[0]" + m[0])
        if m[0]:
            drawable_path = ['drawable', \
                             'drawable-hdpi', \
                             'drawable-hdpi-v4', \
                             'drawable-ldpi', \
                             'drawable-ldpi-v4', \
                             'drawable-mdpi', \
                             'drawable-mdpi-v4', \
                             'drawable-xhdpi', \
                             'drawable-xhdpi-v4', \
                             'drawable-xxhdpi', \
                             'drawable-xxhdpi-v4', \
                             'drawable-xxxhdpi', \
                             'drawable-xxxhdpi-v4', \
                             'mipmap', \
                             'mipmap-hdpi', \
                             'mipmap-hdpi-v4', \
                             'mipmap-ldpi', \
                             'mipmap-ldpi-v4', \
                             'mipmap-mdpi', \
                             'mipmap-mdpi-v4', \
                             'mipmap-xhdpi', \
                             'mipmap-xhdpi-v4', \
                             'mipmap-xxhdpi', \
                             'mipmap-xxhdpi-v4', \
                             'mipmap-xxxhdpi', \
                             'mipmap-xxxhdpi-v4']
            for p in drawable_path:
                if os.path.exists(icon_path + p + '/icon.png'):
                    self.jfprint(self.singleworkingdir + 'temp/res/' + p)
                    self.jfprint(os.path.exists(self.singleworkingdir + 'temp/res/' + p))
                    if os.path.exists(self.singleworkingdir + 'temp/res/' + p):
                        self.jfprint(os.renames(icon_path + p + '/icon.png', icon_path + p + '/' + m[0]))
                    else:
                        shutil.rmtree(icon_path + p)

    def downloadgame(self, gameurl, mappname, singleworkingdir):
        self.jfprint('Beginning file download with urllib2...')
        url = gameurl
        savefileurl = singleworkingdir + mappname + '.apk'
        self.jfprint('下载的url：' + url + '保存的位置：' + savefileurl)
        if os.path.exists(savefileurl):
            self.jfprint('游戏包存在，不再下载:' + savefileurl)
        else:
            urllib.request.urlretrieve(url, savefileurl)
            self.jfprint('下载成功，保存的地址：' + savefileurl)

    def unzip(self, zip_path, extract_path, is_remove=True):
        if os.path.exists(zip_path):
            if zipfile.is_zipfile(zip_path):
                os.mkdir(extract_path)
                f = zipfile.ZipFile(zip_path, 'r')
                for file in f.namelist():
                    f.extract(file, extract_path)
                if is_remove:
                    os.remove(zip_path)
                return True

    def downloadgameicon(self, gameiconurl, mappname, singleworkingdir):
        print('Beginning file download with urllib2...')

        url = gameiconurl
        savefileurl = singleworkingdir + mappname + '.png'
        if os.path.exists(savefileurl):
            self.jfprint('图片存在，不需要下载')
        else:
            self.jfprint('下载的iconurl：' + url)
            urllib.request.urlretrieve(url, savefileurl)
            self.jfprint('下载成功，icon保存的地址：' + savefileurl)
        return savefileurl

    # 下载图片后处理图片创建文件夹的工具方法
    def mkdirj(self, path):
        # 去除首位空格
        path = path.strip()
        # 去除尾部 \ 符号
        path = path.rstrip(os.path.sep + "")

        # 判断路径是否存在
        # 存在     True
        # 不存在   False
        isExists = os.path.exists(path)

        # 判断结果
        if not isExists:
            # 如果不存在则创建目录
            # 创建目录操作函数
            os.makedirs(path)

            # print path+' create new file'
            return True
        else:
            # 如果目录存在则不创建，并提示目录已存在
            # print path+' diretory aready exit'
            shutil.rmtree(path);
            # print path+' dele old file'
            self.mkdirj(path)
            # return False

    # 修改图片
    def img_size(self, inputfilename, outputfilenname, width, height):
        """ fileName: 图片路径,width:修改宽度,height:修改高度 """
        from PIL import Image  # 导入相应模块
        image = Image.open(inputfilename)
        w, h = image.size  # 原图尺寸
        new_img = image.resize((width, height), Image.ANTIALIAS)
        _new = outputfilenname
        new_img.save(_new)
        new_img.close()

    # 创建iconzip文件
    def createiconzipfile(self, inputfilename, workdir):
        # 定义要创建的目录
        self.jfprint("iconname：" + inputfilename)
        tepfilename = 'png';
        if inputfilename.endswith(tepfilename):
            self.jfprint('这个是png文件')
        else:
            self.jfprint('图片不是png的')
        mktemdirpath = os.path.join(workdir, "temdir")
        mktemdirrespath = os.path.join(mktemdirpath, "res")
        self.jfprint(mktemdirrespath)
        mktemdirrespathdrawable = os.path.join(mktemdirrespath, "drawable")
        mktemdirrespathdrawalbeldpi = os.path.join(mktemdirrespath, "drawalbe-ldpi")
        mktemdirrespathdrawablemdpi = os.path.join(mktemdirrespath, "drawable-mdpi")
        mktemdirrespathdrawablehdpi = os.path.join(mktemdirrespath, "drawable-hdpi")
        mktemdirrespathdrawablexhdpi = os.path.join(mktemdirrespath, "drawable-xhdpi")
        mktemdirrespathdrawablexxhdpi = os.path.join(mktemdirrespath, "drawable-xxhdpi")
        mktemdirrespathdrawablexxxhdpi = os.path.join(mktemdirrespath, "drawable-xxxhdpi")

        mktemdirrespathdrawalbeldpiv4 = os.path.join(mktemdirrespath, "drawalbe-ldpi-v4")
        mktemdirrespathdrawablemdpiv4 = os.path.join(mktemdirrespath, "drawable-mdpi-v4")
        mktemdirrespathdrawablehdpiv4 = os.path.join(mktemdirrespath, "drawable-hdpi-v4")
        mktemdirrespathdrawablexhdpiv4 = os.path.join(mktemdirrespath, "drawable-xhdpi-v4")
        mktemdirrespathdrawablexxhdpiv4 = os.path.join(mktemdirrespath, "drawable-xxhdpi-v4")
        mktemdirrespathdrawablexxxhdpiv4 = os.path.join(mktemdirrespath, "drawable-xxxhdpi-v4")

        mktemdirrespathmipmap = os.path.join(mktemdirrespath, "mipmap")
        mktemdirrespathmipmaphdpi = os.path.join(mktemdirrespath, "mipmap-hdpi")
        mktemdirrespathmipmapldpi = os.path.join(mktemdirrespath, "mipmap-ldpi")
        mktemdirrespathmipmapmdpi = os.path.join(mktemdirrespath, "mipmap-mdpi")
        mktemdirrespathmipmapxhdpi = os.path.join(mktemdirrespath, "mipmap-xhdpi")
        mktemdirrespathmipmapxxhdpi = os.path.join(mktemdirrespath, "mipmap-xxhdpi")
        mktemdirrespathmipmapxxxhdpi = os.path.join(mktemdirrespath, "mipmap-xxxhdpi")

        # 调用函数
        self.mkdirj(mktemdirpath)
        self.mkdirj(mktemdirrespath)

        self.mkdirj(mktemdirrespathdrawable)

        self.mkdirj(mktemdirrespathdrawablemdpi)
        self.mkdirj(mktemdirrespathdrawablehdpi)
        self.mkdirj(mktemdirrespathdrawablexhdpi)
        self.mkdirj(mktemdirrespathdrawablexxhdpi)
        self.mkdirj(mktemdirrespathdrawablexxxhdpi)

        self.mkdirj(mktemdirrespathdrawablemdpiv4)
        self.mkdirj(mktemdirrespathdrawablehdpiv4)
        self.mkdirj(mktemdirrespathdrawablexhdpiv4)
        self.mkdirj(mktemdirrespathdrawablexxhdpiv4)
        self.mkdirj(mktemdirrespathdrawablexxxhdpiv4)

        self.mkdirj(mktemdirrespathmipmap)
        self.mkdirj(mktemdirrespathmipmapmdpi)
        self.mkdirj(mktemdirrespathmipmaphdpi)
        self.mkdirj(mktemdirrespathmipmapxhdpi)
        self.mkdirj(mktemdirrespathmipmapxxhdpi)
        self.mkdirj(mktemdirrespathmipmapxxxhdpi)

        iconpath = inputfilename

        self.img_size(iconpath, mktemdirrespathdrawable + os.path.sep + "icon.png", 192, 192)

        self.img_size(iconpath, mktemdirrespathdrawablexxxhdpi + os.path.sep + "icon.png", 192, 192)
        self.img_size(iconpath, mktemdirrespathdrawablexxhdpi + os.path.sep + "icon.png", 144, 144)
        self.img_size(iconpath, mktemdirrespathdrawablexhdpi + os.path.sep + "icon.png", 96, 96)
        self.img_size(iconpath, mktemdirrespathdrawablehdpi + os.path.sep + "icon.png", 72, 72)
        self.img_size(iconpath, mktemdirrespathdrawablemdpi + os.path.sep + "icon.png", 48, 48)

        self.img_size(iconpath, mktemdirrespathdrawablexxxhdpiv4 + os.path.sep + "icon.png", 192, 192)
        self.img_size(iconpath, mktemdirrespathdrawablexxhdpiv4 + os.path.sep + "icon.png", 144, 144)
        self.img_size(iconpath, mktemdirrespathdrawablexhdpiv4 + os.path.sep + "icon.png", 96, 96)
        self.img_size(iconpath, mktemdirrespathdrawablehdpiv4 + os.path.sep + "icon.png", 72, 72)
        self.img_size(iconpath, mktemdirrespathdrawablemdpiv4 + os.path.sep + "icon.png", 48, 48)

        self.img_size(iconpath, mktemdirrespathmipmap + os.path.sep + "icon.png", 192, 192)
        self.img_size(iconpath, mktemdirrespathmipmapxxxhdpi + os.path.sep + "icon.png", 192, 192)
        self.img_size(iconpath, mktemdirrespathmipmapxxhdpi + os.path.sep + "icon.png", 144, 144)
        self.img_size(iconpath, mktemdirrespathmipmapxhdpi + os.path.sep + "icon.png", 96, 96)
        self.img_size(iconpath, mktemdirrespathmipmaphdpi + os.path.sep + "icon.png", 72, 72)
        self.img_size(iconpath, mktemdirrespathmipmapmdpi + os.path.sep + "icon.png", 48, 48)

        targeticonzip = os.path.join(workdir, "icon")

        shutil.make_archive(targeticonzip, 'zip', mktemdirpath)
        self.jfprint('icon压缩成zip成功')

    def jfprint(self, strlog):
        print(strlog)
        self.apkLogPath = self.singleworkingdir + 'log.txt'
        # print(self.apkLogPath)
        self.w_file(self.apkLogPath, strlog)

    def w_file(self, filepath, strlog):
        f = open(filepath, 'a+')
        f.write(strlog + '\n')
        f.close()


'''
{
    "packageName" : "com.yl.zsbb.vivo",
    "targetSdkVersion" : "26",
    "sdk" : "Sdkvivo",
    "baseGameUrl" : "https://d.apps.yayawan.com/apk/0ceshi/zsbb_qjzj_vivo.apk",
    "gameAppId" : "kk4124732690",
    "appName" : "wjzr",
    "gameIconUrl" : 'https://att.yayawan.com/upload/2019/10/11/fc963b016e.png?imageView2/1/w/250/h/250'
    metaData :{
    
    "isDebug" : "true",
    "isLandscape" : "true",
    "sdktype":"string1",
    "copyright":"广州千骐动漫",
    "APPID":"68b1ab4194a6298d37937ee7cf3d897a",
    "APPKEY":"58e6e2610bf42ee611cb8b98d326f029",
    "CPID":"01c8acc92fb831e46150"
    
    }
    
}

'''

if __name__ == "__main__":
    print('123')
    au = AutoPack()
    au.jfprint('123')
    au.jfprint('ceshi')
    au.jfprint('ceshi123')
    au.jfprint('ceshi123')

    # au.setparam('/mnt/e/linuxwenjian/packingapp/share', 'com.yl.zsbb.vivo', '26', 'Sdkvivo', 'https://d.apps.yayawan.com/apk/0ceshi/zsbb_qjzj_vivo.apk', 'kk4124732690',  'wjzr', 'https://att.yayawan.com/upload/2019/10/11/fc963b016e.png?imageView2/1/w/250/h/250', '{"sdktype":"string1","copyright":"广州千骐动漫","APPID":"68b1ab4194a6298d37937ee7cf3d897a","APPKEY":"58e6e2610bf42ee611cb8b98d326f029","CPID":"01c8acc92fb831e46150"}')
    # au.before()
    # au.setparam()
    # au.downloadgameicon('https://att.yayawan.com/upload/2019/10/11/fc963b016e.png?imageView2/1/w/250/h/250', 'zjf', '/mnt/e/linuxwenjian/packingapp/share/')
    # au.createiconzipfile('/mnt/e/linuxwenjian/packingapp/share/zjf.png', '/mnt/e/linuxwenjian/packingapp/share/')
    # print(au.metaData)
    # au.downLoadGame("nihao")
