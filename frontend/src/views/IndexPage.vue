<script setup>
import {get, logout} from "@/net/index.js";
import router from "@/router/index.js";
import {useStore} from "@/store/index.js";
import {reactive, ref} from "vue";
import {
  Back,
  Bell,
  ChatDotSquare, Collection, DataLine,
  Document, Files,
  Location, Lock, Message, Monitor,
  Notification, Operation,
  Position,
  School, Search,
  Umbrella, User
} from "@element-plus/icons-vue";

const store = useStore()
const loading = ref(true)
const searchItem = reactive({
  text: '',
  type: '1'
})

get("/api/user/info",(data)=>{
  store.user = data
  loading.value = false
})

function userLogout(){
  logout(() => router.push("/"))
}
</script>

<template>
  <div class="main-content" v-loading="loading" element-loading-text="正在加载，请稍后...">
    <el-container style="height: 100%" v-if="!loading">
      <el-header class="main-content-header">
        <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"></el-image>
        <div style="flex: 1;text-align: center">
          <el-input v-model="searchItem.text" style="padding: 0 20px;width: 100%;max-width: 500px" placeholder="请输入搜索内容...">
            <template #prefix>
              <el-icon><Search/></el-icon>
            </template>
            <template #append>
              <el-select v-model="searchItem.type" style="width: 120px">
                <el-option value="1" label="校园活动"></el-option>
                <el-option value="2" label="表白墙"></el-option>
                <el-option value="3" label="教务通知"></el-option>
                <el-option value="4" label="成绩查询"></el-option>
              </el-select>
            </template>
          </el-input>
        </div>
        <div class="user-info">
          <div class="profile">
            <div>{{store.user.username}}</div>
            <div>{{ store.user.email }}</div>
          </div>
          <el-dropdown>
            <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
            <template #dropdown>
              <el-dropdown-item>
                <el-icon><Operation/></el-icon>
                个人信息
              </el-dropdown-item>
              <el-dropdown-item>
                <el-icon><Message/></el-icon>
                信息列表
              </el-dropdown-item>
              <el-dropdown-item @click="userLogout" divided>
                <el-icon><Back/></el-icon>
                退出登录
              </el-dropdown-item>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="220px">
          <el-scrollbar style="height: calc(100vh - 55px)">
            <el-menu
                router
                :default-active="$route.path"
                style="min-height: calc(100vh - 55px)"
            >
              <el-sub-menu index="1">
                <template #title>
                  <el-icon><Location/></el-icon>
                  <span><b>校园论坛</b></span>
                </template>
                <el-menu-item index="1-1">
                  <template #title>
                    <el-icon><ChatDotSquare/></el-icon>
                    <span>帖子广场</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Bell/></el-icon>
                    <span>失物招领</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Notification/></el-icon>
                    <span>校园活动</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Umbrella/></el-icon>
                    <span>表白墙</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><School/></el-icon>
                    <span>海文考研</span>
                    <el-tag style="margin-left: 10px" size="small">合作机构</el-tag>
                  </template>
                </el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="2">
                <template #title>
                  <el-icon><Position/></el-icon>
                  <span><b>探索与发现</b></span>
                </template>
                <el-menu-item>
                  <template #title>
                    <el-icon><Document/></el-icon>
                    <span>成绩查询</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Files/></el-icon>
                    <span>班级课程表</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Monitor/></el-icon>
                    <span>教务通知</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Collection/></el-icon>
                    <span>在线图书馆</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><DataLine/></el-icon>
                    <span>预约教室</span>
                  </template>
                </el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="3">
                <template #title>
                  <el-icon><Operation/></el-icon>
                  <span><b>个人设置</b></span>
                </template>
                <el-menu-item index="/index/user-setting">
                  <template #title>
                    <el-icon><User/></el-icon>
                    <span>个人信息设置</span>
                  </template>
                </el-menu-item>
                <el-menu-item index="/index/privacy-setting">
                  <template #title>
                    <el-icon><Lock/></el-icon>
                    <span>账号安全设置</span>
                  </template>
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-scrollbar>
        </el-aside>
          <el-main class="main-content-page">
            <el-scrollbar style="height: calc(100vh - 55px)">
              <router-view v-slot="{ Component }">
                <transition name="el-fade-in-linear" mode="out-in">
                  <Component :is="Component"/>
                </transition>
              </router-view>
            </el-scrollbar>

          </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<style lang="less" scoped>
.main-content {
  width: 100vw;
  height: 100vh;
}
.main-content-page {
  padding: 0;
  background-color: #f7f8fa;
}
.dark .main-content-page {
  background-color: #212225;
}
.main-content-header {
  height: 55px;
  display: flex;
  align-items: center;
  border-bottom: solid 1px var(--el-border-color);
  box-sizing: border-box;
  .logo {
    height: 32px;
  }
  .user-info {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    .el-avatar:hover {
      cursor: pointer;
    }
    .profile {
      text-align: right;
      margin-right: 20px;

      :first-child {
        font-size: 18px;
        font-weight: bold;
        line-height: 20px;
      }

      :last-child {
        font-size: 10px;
        color: grey;
      }
    }
  }
}


</style>