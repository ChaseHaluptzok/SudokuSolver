git add .
git commit -a -m "COMMIT_MESSAGE"
git push origin master
set tag=%1
git tag -a %tag% -m "TAG_MESSAGE"
git push origin %tag%

REM push [tag]