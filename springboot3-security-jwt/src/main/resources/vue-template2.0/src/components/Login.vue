<template>
  <!-- style="font-family:kaiti" -->
    <div class="background"  >

<!-- 注册表单,一个对话框 -->
<el-dialog v-model="isRegister"   title="用户注册"   width="30%"  draggable=true>
    <el-form label-width="120px" v-model="registerForm">
        <el-form-item label="用户名">
            <el-input type="text"   v-model="registerForm.username"   >
              <template #prefix>
                <el-icon><Avatar /></el-icon>
              </template>
              
            </el-input>
        </el-form-item>
        <el-form-item label="密码">
            <el-input  type="password" v-model="registerForm.password" >
              <template #prefix>
        <el-icon><Lock /></el-icon>
        </template>
            </el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="registerAdd" >提交</el-button>
            <el-button @click="isRegister = false">取消</el-button>
        </el-form-item>
    </el-form>
</el-dialog>

<!-- 登陆框 -->
<div class="login-box">
<el-form
    label-width="100px"
    :model="loginFrom"
    style="max-width: 460px"
    :rules="Loginrules"
    ref="ruleFormRef"
  >
    <el-form-item label="用户名"  prop="username">
      <el-input v-model="loginFrom.username"  clearable  >
        <template #prefix>
                <el-icon><Avatar /></el-icon>
              </template>
        </el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">

      <el-input v-model="loginFrom.password"   show-password   clearable  type="password" >
        <template #prefix>
        <el-icon><Lock /></el-icon>
        </template>
      </el-input>
    </el-form-item>

    <el-form-item label="验证码"  prop="codeValue">
      <el-input v-model="loginFrom.codeValue"  style="width: 100px;"  clearable  >
      </el-input>

      <img :src="codeImage" @click="getCode" style="transform: scale(0.9);"/>
    </el-form-item>
<el-form-item >

 <!-- <el-checkbox v-model="rememberMe.rememberMe"   >记住我</el-checkbox>  -->


</el-form-item>

    <el-button type="success" @click="getLogin(ruleFormRef)"  style="transform: translateX(50px)"  class="my-button">登录</el-button>
    <el-button type="primary" @click="isRegister=true"   class="my-button">注册</el-button>
  </el-form>

</div>

    </div>
</template>

<script lang="ts" setup>
import { ref,onMounted,reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import useTokenStore  from '@/stores/useToken'
import  api  from '@/utils/request'
import type { FormInstance } from 'element-plus'
// import rememberMeStore from '@/stores/rememberMe'

const aa=ref("")
const bb=ref(10)

aa.value="12345678900000"
bb.value=bb.value+10000

console.log(aa.value);
console.log(bb.value);



const ruleFormRef =  ref<FormInstance>()

// const rememberMe=rememberMeStore()

const loginFrom=ref({
username:'',
password:'',
codeKey:'',
codeValue:''
// rememberMe:rememberMe.rememberMe
})

const Loginrules=reactive({

  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur'}
  ],
  codeValue: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]

})

// const rememberMeLogin=async()=>{
// if(rememberMe.rememberMe){

// let data:any=await api.post('/user/rememberMe',loginFrom.value)
// if(data.code===200){
//   ElMessage.success('登录成功')
//   router.replace({name:'layout'})
// }else{
//   ElMessage.error('登录失败')
// }

// }
// }

// onMounted(async()=>{

//   rememberMeLogin()
// })

const registerForm=ref({
  username:'',
  password:''
})

const codeImage=ref('')

const isRegister=ref(false)

const tokenStore = useTokenStore();


const router = useRouter()



const getLogin = async(formEl: FormInstance | undefined) => {

  if (!formEl)  return

  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log('submit!')
    } else {
      ElMessage('请输入完整信息')
      return;
    }
  })

  let data:any =await api.post('/user/login',loginFrom.value)

if(data.code===200){
  ElMessage('登录成功')
  console.log(data.data);
  tokenStore.token=data.data
  router.replace({name:'layout'})
}else{

  ElMessage('登录失败')
  ElMessage('失败原因：'+data.message)

}


}


const getCode=async()=>{
  let {data}=await api.get('/getCaptcha')
  loginFrom.value.codeKey=data.codeKey
  codeImage.value=data.codeValue

}

const registerAdd=async()=>{
let data:any=await api.post('/user/register',registerForm.value)

if(data.code==200){
  ElMessage('注册成功')
  isRegister.value=false
}else{

  ElMessage('注册失败')
  isRegister.value=false
  }

}

// 页面加载完成获取验证码

onMounted(()=>{
getCode()

})


</script>



<style lang="scss" scoped>
// 登录页面总样式
.background{
  background: url("@/assets/20.png");
  width:100%;
  height:100%;  /**宽高100%是为了图片铺满屏幕 */
  position: fixed;
  background-size: 100% 100%;
  font-family:kaiti
}

.login-box{
  border:1px solid #dccfcf;
  width: 350px;
  margin:180px auto;
  padding: 20px 50px 20px 50px;
  border-radius: 5px;
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  box-shadow: 0 0 25px #909399;
  background-color:rgba(255,255,255,0.7);//这里最后那个0.7就是为了防止背景表单颜色太浅
}


.my-button {
  margin-right: 100px;
}


</style>