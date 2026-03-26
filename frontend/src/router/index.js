import {createRouter, createWebHistory} from "vue-router";
import {isUnauthorizedToken} from "@/net/index.js";

const router = createRouter({
    history:createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path:'/',
            name:'welcome',
            component:()=>import('@/views/WelcomePage.vue'),
            children:[{
                path:'',
                name:'welcome-login',
                component:()=>import('@/views/welcome/LoginPage.vue')
            },
                {
                    path:'/register',
                    name:'welcome-register',
                    component:()=>import('@/views/welcome/RegisterPage.vue')
                },
                {
                    path:'/reset',
                    name:'welcome-reset',
                    component:()=>import('@/views/welcome/ForgetPage.vue')
                }
            ]
        },
        {
            path:'/index',
            name:'index',
            component:()=>import('@/views/IndexPage.vue'),
            children:[
                {
                    path: 'user-setting',
                    name: 'user-setting',
                    component: () => import('@/views/settings/UserSettings.vue')
                },
                {
                    path: 'privacy-setting',
                    name: 'privacy-setting',
                    component: () => import('@/views/settings/PrivacySettings.vue')
                },
                {
                    path: '',
                    name: 'topic-page',
                    component: () => import('@/views/forum/TopicPage.vue')
                }
            ]
        }
    ]
})
router.beforeEach((to,from,next)=>{
    if(to.name.startsWith('welcome') && !isUnauthorizedToken()){
        next('/index')
    }else if(to.fullPath.startsWith('/index') && isUnauthorizedToken()){
        next('/')
    }else next()
})
export default router