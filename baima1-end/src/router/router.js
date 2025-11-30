import {createRouter, createWebHistory} from "vue-router";
import {isUnauthorized} from "@/net/index.js";

const router = createRouter({
    history:createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path:'/',
            name:'welcome',
            component:()=>import("@/views/WelcomeVue.vue"),
            children:[
                {
                    path:'',
                    name:'welcome-login',
                    component:()=>import("@/views/welcome/LoginVue.vue")
                },
                //
                {
                    path:'register',
                    name:'welcome-register',
                    component:()=>import("@/views/welcome/RegisterPage.vue")
                },
                //
                {
                    path:'reset',
                    name:'welcome-reset',
                    component:()=>import("@/views/welcome/ResetPage.vue")
                }
            ]
        },
        {
            path:'/index',
            name:'index',
            component:()=>import('@/views/IndexVue.vue')
        }
    ]
})
router.beforeEach((to,from,next)=>{
    const isUnAuthorized = isUnauthorized()
    if(to.name.startsWith('welcome-') && !isUnAuthorized) next('/index')
    else if(to.fullPath.startsWith('/index')&& isUnAuthorized) next('/')
    else next()

})
export default router