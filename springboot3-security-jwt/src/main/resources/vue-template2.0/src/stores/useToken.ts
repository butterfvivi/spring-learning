import { defineStore } from 'pinia'
import { ref } from 'vue'
const useTokenStore = defineStore('token', ()=>{
const token=ref("")


const removeToken=()=>{
    token.value=""
}

return {token,removeToken}
},
{persist: true}

)

export default useTokenStore

