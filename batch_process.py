
import sys
import os, os.path
import subprocess
import shutil

#tdir_par = "/home/cbw/workspace/UFO/benchmarks/chromium2/src/out/test_12_5_youtb"

tdir_par = '/home/cbw/workspace/UFO/benchmarks/firefox/firefox_1_2'
# tdir_par = "/home/cbw/workspace/UFO/benchmarks/chromium2/src/out/test_brw_12_1"
# args = sys.argv
# if (len(args) != 2):
#     raise ValueError('Illegal Args ' + str(args))

# tdir_par = args[1]
# print("processing execution traces from " + tdir_par)

jar_path = "/home/cbw/workspace/UFO/UFO-Postprocess/out/artifacts/UFO_jar/UFO.jar"
janalyzer_class = 'aser.ufo.Main'
junzip_class = 'aser.ufo.TracePreprocessor'

cmd_jvm = ' -server -Xmx21g -XX:ReservedCodeCacheSize=512m -Xnoclassgc -Xverify:none ' \
          '-XX:+UnlockCommercialFeatures -XX:+UnlockExperimentalVMOptions ' \
          '-Dsun.io.useCanonCaches=false -Djsse.enableSNIExtension=false ' \
          '-XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:SoftRefLRUPolicyMSPerMB=50 ' \
          '-XX:+DoEscapeAnalysis -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -XX:+CMSClassUnloadingEnabled ' \
          '-XX:+CMSParallelRemarkEnabled -XX:+UseCodeCacheFlushing -XX:+OptimizeStringConcat'.split()

cmd_jar_ana = ' -window_size 12000 -solver_timeout 80 -solver_memory 2000'.split()



dir_tmp = "unzipped"


def remove_single_thr_trace():
    for tdir in os.listdir(tdir_par):
        _dir = tdir_par + '/' + tdir
        if os.path.isdir(_dir):
            file_count = len([name for name in os.listdir(_dir) if os.path.isfile(_dir + '/' + name)])
            # print(file_count)
            if (file_count < 3):
                shutil.rmtree(_dir)


def analyze_traces():
    for tdir in os.listdir(tdir_par):
        _dir = tdir_par + '/' + tdir
        if os.path.isdir(_dir):
            try:
                ls = ['java'] + cmd_jvm + ['-cp', jar_path, junzip_class, _dir, dir_tmp]
                subprocess.call(ls)
            except:
                print("Failed to unzip " + _dir + sys.exc_info()[0])
                continue
            try:
                # _dir as app name

                ls = ['java'] + cmd_jvm + ['-cp', jar_path, janalyzer_class, _dir, '-tdir', dir_tmp] + cmd_jar_ana
                subprocess.call(ls)
            except:
                print("Failed to analyze " + _dir + sys.exc_info()[0])
                break
            shutil.rmtree(dir_tmp)

if __name__ == "__main__":
    remove_single_thr_trace()
    analyze_traces()