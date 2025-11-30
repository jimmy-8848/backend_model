import {ElMessage} from "element-plus";
import axios from "axios";

const preToken = 'access_token'
//默认失败
function defaultFailure(url,code,message){
    console.log(`请求地址：${url},错误码:${code},错误信息:${message}`)
    ElMessage.warning(message)
}
//默认成功
function defaultError(err){
    console.log(err)
    ElMessage.warning('发生了一些错误，请联系管理员')
}
//token取
function getAccessToken(){
    const token = sessionStorage.getItem(preToken) || localStorage.getItem(preToken)
    if(!token) return null;
    //由于获取的token是字符串，但是再JS当中要用JSON对象，所以用JSON.parse方法来转换一下
    const tokenObj = JSON.parse(token)
    //还要判断现在的时间是否过期
    if(tokenObj.expire <= new Date()){
        //因为过期了，所以要删掉
        deleteToken()
        ElMessage.warning('令牌过期，请重新登录！')
        return null
    }
    //因为存进去的是字符串形式的对象，所以需要用parse方法转成Obj，再用这个Obj中的token返回
    return tokenObj.token
}
//token删
function deleteToken(){
    sessionStorage.removeItem(preToken)
    localStorage.removeItem(preToken)
}
//token存
function storeToken(token,expire,remember){
    //由于一般是登录的时候存token，所以不需要判断过期时间
    //因为我们要存token本身和过期时间且存到storage中需要字符串，所以要用json对象再转字符串再存到storage
    const tokenObj = {token:token,expire:expire}
    const ObjString = JSON.stringify(tokenObj)
    //如果记住我点上了，那就要存到localstorage否则存到sessionstorage
    if(remember){
        localStorage.setItem(preToken,ObjString)
    }else sessionStorage.setItem(preToken,ObjString)
}
//内部Post
function internalPost(url,data,header,success,failure,error=defaultError){
    axios.post(url,data,{headers:header}).then(({data})=>{
        if(data.code===200) success(data.data)
        else failure(url,data.code,data.message)
    }).catch(err=>{error(err)})
}
//这个post是默认是需要头才能用的，也就是说不能用于登录或者其它不需要令牌的界面,同理get也一样
function post(url,data,success,failure=defaultFailure){
    internalPost(url,data,getAccessHeader(),success,failure)
}
//内部Get
function internalGet(url,header,success,failure,error=defaultError) {
    axios.get(url, {headers: header}).then(({data}) => {
        if (data.code === 200) success(data.data)
        else failure(url, data.code, data.message)
    }).catch(err => {
        error(err)
    })
}
//获取一下带请求头的完整token的对象
function getAccessHeader(){
    const token = getAccessToken()
    return token ? {
        'Authorization':`Bearer ${getAccessToken()}`
    } : {}
}
function get(url,success,failure=defaultFailure){
    internalGet(url,getAccessHeader(),success,failure)
}

//接下来可以写真正的逻辑了
function login(username,password,remember,success,failure=defaultFailure){
    internalPost('api/auth/login',{
        username:username,
        password:password
    },{
        'Content-Type':'application/x-www-form-urlencoded'
    },(data)=>{
        storeToken(data.token,data.expire,remember)
        ElMessage.success(`登录成功，欢迎${data.username} 来到我们的系统`)
        success()
        },
        failure)
}
function logout(success,failure=defaultFailure){
    get('/api/auth/logout',()=>{
        deleteToken()
        ElMessage.success('登出成功！')
        success()
    },failure)
}

function isUnauthorized(){
    return !getAccessToken()
}
export {get,post,logout,login,isUnauthorized}