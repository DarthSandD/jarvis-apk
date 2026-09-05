import zipfile, shutil, os

src = r"C:\Users\USER\jarvis-apk\gradle-8.5\lib\plugins\gradle-wrapper-8.5.jar"
dst = r"C:\Users\USER\jarvis-apk\gradle\wrapper\gradle-wrapper.jar"
tmp = dst + ".new"

shutil.copy2(src, tmp)
print(f"Copied {os.path.getsize(tmp)} bytes")

with zipfile.ZipFile(tmp, 'r') as z:
    for n in z.namelist():
        if 'wrapper' in n.lower() or 'Wrapper' in n:
            print(f"  - {n}")
    if 'org/gradle/wrapper/GradleWrapperMain.class' in z.namelist():
        cls = z.read('org/gradle/wrapper/GradleWrapperMain.class')
        print(f"GradleWrapperMain.class: {len(cls)} bytes - OK")
    else:
        print("WARNING: GradleWrapperMain.class NOT found")
