# git的常用命令

#### 删除已经提交的target .idea等文件

git rm -r --cached .idea/

git rm -r --cached target/

(--cached 表示本地仍旧保留)

git commit -m "删除不需要的文件"

git push


#### 解决冲突