user=$1
pass=$2
db=$3
mkdir ~/Software/MoneyMaker/mmBackup
mysqldump -u $user -p$pass $db > ~/Software/MoneyMaker/mmBackup/mm.sql
