<template>
    
            <div class="common-layout">
    <el-container>

      <el-aside width="200px">Aside</el-aside>
      <el-container>
        
        <el-main>

<br>
<el-button type="primary" @click="getUserInfo">获取用户信息</el-button>
            <el-button type="success" @click="Logout">退出登录</el-button>
       <!--     <el-button type="success" @click="testHello">测试权限</el-button> -->

        </el-main>

        <el-footer>Footer</el-footer>

      </el-container>
    </el-container>
  </div>

</template>

<script lang="ts" setup name="Layout">
import { ref } from 'vue'
import api from '@/utils/request'
import {ElMessage} from 'element-plus'
import { useRouter } from 'vue-router'
import  useToeknStore from '@/stores/useToken'
const router = useRouter()

const useToken=useToeknStore()

const Logout =async () => {
 let data:any= await api.get("/user/logout")
console.log('退出登录====>',data);
if(data.code===200){
  // 清除token
useToken.removeToken()

ElMessage('退出成功')

router.replace({name:'login'})

}
else{
  ElMessage.error('退出失败')
}

}

const getUserInfo = async() => {

let data=await api.get("/user/info")

console.log('@',data);

}


const testHello = async() => {
  let data:any= await api.get("/test/hello")
if(data.code===200){
  ElMessage('有权限')
}
else{
  ElMessage.error('没有权限')
}
}

</script>

<style lang="scss" scoped>
.el-aside {
  
background: black;

height: calc(100vh);

}
.el-header {

background: #fff;


}
.el-main {
background: #f0f2f5;
}
.el-footer {
background: #fff;
}

</style>