import {defineStore} from "pinia";
import {computed} from "vue";
import axios from "axios";

export const useStore = defineStore("general",{
    state:()=>{
        return {
            user:{
                username:'',
                email:'',
                avatar:null,
                role:'',
                registerTime:null
            }
        }
    },
    getters:{
        avatarUrl(){
            if(!this.user.avatar) return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
            else return axios.defaults.baseURL+`/images${this.user.avatar}`
        }
    }
})