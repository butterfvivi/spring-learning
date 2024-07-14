import { defineStore } from 'pinia'
import { ref } from 'vue'
const rememberMeStore = defineStore('rememberMe', ()=>{
const rememberMe=ref(false)




return {rememberMe}
},
{persist: true}
)

export default rememberMeStore
