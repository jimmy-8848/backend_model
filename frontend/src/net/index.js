import {ElMessage} from "element-plus";
import axios from "axios";

const access_token = 'access_token'

//默认失败和错误方法
function defaultFailure(url,code,message){
    console.warn(`错误路径:${url},错误码:${code},错误信息:${message}`)
    ElMessage.warning(message)
}

function defaultError(error){
    console.error(error)
    ElMessage.error('发生了一些错误，请联系管理员')
}

//token存 取 删
function storeToken(token,expire,remember){
    const tokenObj = {token,expire}
    const tokenObjString = JSON.stringify(tokenObj)
    if(remember) localStorage.setItem(access_token,tokenObjString)
    else sessionStorage.setItem(access_token,tokenObjString)
}

function deleteToken(){
    localStorage.removeItem(access_token)
    sessionStorage.removeItem(access_token)
}

function getToken(){
    const tokenObjString =     localStorage.getItem(access_token) || sessionStorage.getItem(access_token)
    if(tokenObjString) {
        const tokenObj = JSON.parse(tokenObjString)
        if(tokenObj.expire <= new Date()) {
            deleteToken()
            ElMessage.warning('令牌过期，请重新登录')
            return null
        }
        else return tokenObj.token
    }else{
        return null
    }
}

//获取token请求头
function getAccessHeader(){
    const token = getToken()
    return token?{Authorization:`Bearer ${token}`}:{}
}

//通用post和get方法
function internalPost(url,data,header,success,failure,err=defaultError){
    axios.post(url,data,{headers:header}).then(({data})=>{
        if(data.code ===200) success(data.data)
        else failure(url,data.code,data.message)
    }).catch(error=>err(error))
}

function internalGet(url,header,success,failure,err=defaultError){
    axios.get(url,{headers:header}).then(({data})=>{
        if(data.code ===200) success(data.data)
        else failure(url,data.code,data.message)
    }).catch(error=>err(error))
}

//自带token的post和get方法
function post(url,data,success,failure=defaultFailure){
    internalPost(url,data,getAccessHeader(),success,failure)
}
function get(url,success,failure=defaultFailure){
    internalGet(url,getAccessHeader(),success,failure)
}

//login方法
function login(username,password,remember,success){
    internalPost('/auth/login',{username:username,password:password},
        {'Content-Type':'application/x-www-form-urlencoded'},
        (data)=>{
        ElMessage.success(`登录成功，欢迎${data.username}进入我们的系统`)
            storeToken(data.token,data.expire,remember)
            success()
        },defaultFailure)
}

//logout方法
function logout(success){
    get('/auth/logout',()=>{
        ElMessage.success('退出登录成功')
        deleteToken()
        success()
    })
}

//是否有token
function isUnauthorizedToken(){
    return !getToken()
}
export {isUnauthorizedToken,login,logout,post,get}