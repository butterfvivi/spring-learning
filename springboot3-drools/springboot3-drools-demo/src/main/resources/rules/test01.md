
curl --location --request POST 'http://localhost:8080/drools/rule/add' \
--header 'User-Agent: apifox/1.0.0 (https://www.apifox.cn)' \
--header 'Content-Type: application/json' \
--data-raw '{
"ruleId": 1,
"kieBaseName": "kieBase01",
"kiePackageName": "rules.rule01",
"ruleContent": "package rules.rule01 \n global java.lang.StringBuilder resultInfo \n rule \"rule-01\" when $i: Integer() then resultInfo.append(drools.getRule().getPackageName()).append(\".\").append(drools.getRule().getName()).append(\"执行了，前端传递的参数:\").append($i); end"
}'