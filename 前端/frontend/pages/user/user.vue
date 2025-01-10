<template>
	<view class="container">
		<!-- 头像和昵称 -->
		<view class="profile-section">
			<view class="avatar" @click="chooseAvatar">
				<!-- 如果 avatarUrl 存在，则显示图片，否则显示占位符 -->
				<image :src="avatarUrl" mode="aspectFill" class="avatar-img" v-if="avatarUrl"></image>
				<text v-else class="placeholder">+</text>
			</view>
			<!-- 昵称和用户名部分 -->
			<view class="text-section">
				<view class="label-and-value">
					<text class="label nickname-label">昵称:</text>
					<view class="nickname" @click="setNickname">{{ nickname || '设置昵称' }}</view>
				</view>
				<view class="label-and-value">
					<text class="label username-label">用户名:</text>
					<view class="username">{{ username || '用户名' }}</view>
				</view>
			</view>
		</view>

		<!-- 修改昵称弹窗 -->
		<view v-if="showNicknamePopup" class="popup-overlay">
			<view class="popup-container">
				<view class="popup-title">修改昵称</view>
				<input v-model="newNickname" placeholder="请输入新的昵称" class="popup-input" />
				<button class="register-btn" @tap="confirmNicknameChange">确认修改</button>
				<view class="close-btn" @tap="closeNicknamePopup">×</view>
			</view>
		</view>
		
		
<!-- 添加词书弹窗 -->
    <view v-if="showAddBookPopup" class="popup-overlay">
      <view class="popup-container">
        <view class="popup-title">添加词书</view>
        <input v-model="newBook.title" placeholder="请输入词书标题" class="popup-input" />
        <input v-model="newBook.description" placeholder="请输入词书描述" class="popup-input" />
        <view class="checkbox-container">
          <checkbox :checked="newBook.isPublic === 1" @change="toggleIsPublic(1)">公开</checkbox>
          <checkbox :checked="newBook.isPublic === 0" @change="toggleIsPublic(0)">私密</checkbox>
        </view>
        <button class="register-btn" @tap="confirmAddBook">确认添加</button>
        <view class="close-btn" @tap="closeAddBookPopup">×</view>
      </view>
    </view>


				
				
		<!-- 功能选项 -->
		<view class="list-item" @click="goToWordBookPage">
			<text class="item-text">删改查和修改默认词书</text>
			<text class="arrow">></text>
		</view>
		<view class="list-item" @click="addBook">
			<text class="item-text">添加词书</text>
			<text class="arrow">></text>
		</view>
		
		<view class="list-item" @click="goToImportWordBookPage">
			<text class="item-text">导入词书</text>
			<text class="arrow">></text>
		</view>
		
		<view class="list-item" @click="goToShowStats">
			<text class="item-text">数据统计</text>
			<text class="arrow">></text>
		</view>
		
		<view class="list-item" @click="goToLogin">
			<text class="item-text">回到登录页面</text>
			<text class="arrow">></text>
		</view>
	</view>
</template>

<script>
export default {
  data() {
    return {
      avatarUrl: "", // 存储用户头像地址
      nickname: "", // 存储用户昵称
      username: "", // 存储用户名
      showNicknamePopup: false, // 控制昵称弹窗显示与否
      newNickname: "", // 存储新的昵称输入值
      showAddBookPopup: false, // 控制添加词书弹窗显示
      newBook: {
        title: "",
        description: "",
        isPublic: 0, // 默认选中“私密”
      },
    };
  },
  created() {
    this.fetchAvatarUrl(); // 初始化时获取头像地址
    this.fetchUserInfo();
    console.log("isPublic 初始化:", this.newBook.isPublic, typeof this.newBook.isPublic);
  },
  methods: {
    /** 用户头像相关方法 **/
    // 获取用户头像地址
    fetchAvatarUrl() {
      uni.request({
        url: "http://localhost:9192/user/getAvatarUrl",
        method: "GET",
        header: {
          token: uni.getStorageSync("token"),
        },
        success: (res) => {
          if (res.data.code === 1) {
            this.avatarUrl = res.data.data;
            console.log("头像地址获取成功:", this.avatarUrl);
          } else {
            console.error("获取头像失败:", res.data.msg);
            uni.showToast({ title: res.data.msg || "获取头像失败", icon: "none" });
          }
        },
        fail: (err) => {
          console.error("网络请求失败:", err);
          uni.showToast({ title: "网络错误，请重试", icon: "none" });
        },
      });
    },

    // 选择并上传头像
    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ["compressed"],
        sourceType: ["album", "camera"],
        success: (res) => {
          const filePath = res.tempFilePaths[0];
          this.uploadAvatar(filePath);
        },
      });
    },

    // 上传头像到后端
    uploadAvatar(filePath) {
      uni.uploadFile({
        url: "http://localhost:9192/user/upload",
        filePath,
        name: "image",
        header: {
          token: uni.getStorageSync("token"),
        },
        success: (uploadRes) => {
          const data = JSON.parse(uploadRes.data);
          if (data.code === 1) {
            uni.showToast({ title: "上传成功", icon: "none" });
            this.fetchAvatarUrl();
          } else {
            console.error("上传失败:", data.msg);
            uni.showToast({ title: data.msg || "上传失败", icon: "none" });
          }
        },
        fail: (err) => {
          console.error("网络请求失败:", err);
          uni.showToast({ title: "网络错误，请重试", icon: "none" });
        },
      });
    },

    /** 用户信息相关方法 **/
    // 获取用户昵称和用户名
    fetchUserInfo() {
      uni.request({
        url: "http://localhost:9192/user/getNicknameAndUsername",
        method: "GET",
        header: {
          token: uni.getStorageSync("token"),
        },
        success: (res) => {
          if (res.data.code === 1) {
            this.nickname = res.data.data.nickname;
            this.username = res.data.data.username;
          } else {
            uni.showToast({ title: res.data.msg || "获取用户信息失败", icon: "none" });
          }
        },
        fail: (err) => {
          uni.showToast({ title: "网络错误，请重试", icon: "none" });
        },
      });
    },

    // 设置昵称
    setNickname() {
      this.newNickname = this.nickname;
      this.showNicknamePopup = true;
    },

    // 关闭昵称弹窗
    closeNicknamePopup() {
      this.showNicknamePopup = false;
    },

    // 确认修改昵称
	confirmNicknameChange() {
	  if (this.newNickname.trim()) {
		// 调用后端接口更新昵称
		uni.request({
		  url: `http://localhost:9192/user/updateNickname?nickname=${encodeURIComponent(this.newNickname.trim())}`,
		  method: "PUT",
		  header: {
			token: uni.getStorageSync("token"), // 携带用户 token
		  },
		  success: (res) => {
			if (res.data.code === 1) {
			  // 更新本地昵称
			  this.nickname = this.newNickname.trim();
			  uni.showToast({ title: "昵称修改成功", icon: "none" });
			} else {
			  uni.showToast({ title: res.data.msg || "修改失败", icon: "none" });
			}
		  },
		  fail: (err) => {
			uni.showToast({ title: "网络错误，请重试", icon: "none" });
		  },
		});
	  } else {
		uni.showToast({ title: "请输入有效昵称", icon: "none" });
	  }
	  this.closeNicknamePopup();
	},


    /** 添加词书相关方法 **/
    // 切换公开/私密状态
    toggleIsPublic(value) {
      this.newBook.isPublic = value;
    },

    // 添加词书
    addBook() {
      this.newBook = { title: "", description: "", isPublic: 0 };
      this.$nextTick(() => {
        this.showAddBookPopup = true;
      });
    },

    // 确认添加词书
    confirmAddBook() {
      const { title, description, isPublic } = this.newBook;
      if (!title.trim() || !description.trim()) {
        uni.showToast({ title: "请输入完整信息", icon: "none" });
        return;
      }

      uni.request({
        url: "http://localhost:9192/wordBook/add",
        method: "POST",
        header: {
          token: uni.getStorageSync("token"),
          "Content-Type": "application/json",
        },
        data: { name: title, description, isPublic },
        success: (res) => {
          if (res.data.code === 1) {
            uni.showToast({ title: "添加成功", icon: "none" });
            this.closeAddBookPopup();
          } else {
            uni.showToast({ title: res.data.msg || "添加失败", icon: "none" });
          }
        },
        fail: (err) => {
          uni.showToast({ title: "网络错误，请重试", icon: "none" });
        },
      });
    },

    // 关闭添加词书弹窗
    closeAddBookPopup() {
      this.showAddBookPopup = false;
    },

    /** 其他功能方法 **/
    // 修改默认词书
	goToWordBookPage() {
		  uni.navigateTo({ url: '/pages/wordBookPage/wordBookPage' });
		},
	
	goToImportWordBookPage() {
	  uni.navigateTo({ url: '/pages/importWordBookPage/importWordBookPage' });
	},
    // 查看数据统计
    goToShowStats() {
      uni.navigateTo({ url: '/pages/ShowStats/ShowStats' });
    },
	goToLogin(){
	  uni.navigateTo({ url: '/pages/login/login' });
	}
  },
};
</script>

<style>
.container {
	padding: 10px;
	background-color: #f8f8f8;
	min-height: 100vh;
}

/* 头像和昵称部分 */
.profile-section {
	display: flex;
	align-items: center;
	padding: 15px;
	background-color: #ffffff;
	border-radius: 8px;
	margin-bottom: 10px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.avatar {
	width: 60px;
	height: 60px;
	border-radius: 50%;
	overflow: hidden;
	background-color: #eaeaea;
	display: flex;
	justify-content: center;
	align-items: center;
}

.avatar-img {
	width: 100%;
	height: 100%;
	border-radius: 50%;
}

.placeholder {
	font-size: 24px;
	color: #999;
}

.text-section {
	display: flex;
	flex-direction: column;
	justify-content: center;
	margin-left: 15px;
	width: 100%; /* 占满父级容器 */
	overflow: hidden; /* 防止内容超出布局 */
}

.nickname {
	margin-left: 15px;
	font-size: 16px;
	color: #333;
}

.username {
	font-size: 14px;
	color: #999;
}
/* 功能选项部分 */
.list-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 15px;
	border-bottom: 1px solid #eaeaea;
	background-color: #ffffff;
	border-radius: 8px;
	margin-bottom: 10px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.item-text {
	font-size: 16px;
	color: #333;
}

.arrow {
	font-size: 16px;
	color: #999;
}
.popup-overlay {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0.5);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 1000;
}

.popup-container {
	background-color: #ffffff;
	border-radius: 8px;
	padding: 20px;
	width: 80%;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.popup-title {
	font-size: 18px;
	font-weight: bold;
	margin-bottom: 15px;
	text-align: center;
}

.popup-input {
	width: 100%;
	padding: 10px;
	margin-bottom: 15px;
	border: 1px solid #eaeaea;
	border-radius: 4px;
	font-size: 16px;
}

.register-btn {
	width: 100%;
	padding: 10px;
	background-color: #4caf50;
	color: #ffffff;
	border: none;
	border-radius: 4px;
	text-align: center;
	font-size: 16px;
}

.close-btn {
	position: absolute;
	top: 10px;
	right: 10px;
	font-size: 20px;
	color: #999;
	cursor: pointer;
}
.label-and-value {
	display: flex;
	align-items: center; /* 保持水平对齐 */
	flex-wrap: nowrap;   /* 防止内容自动换行 */
	margin-bottom: 8px;  /* 间距维持不变 */
}


.label {
	margin-right: 2px;
}

.nickname-label {
	font-size: 20px; /* 昵称的标签字体变大 */
	color: #333;
	font-weight: bold; /* 昵称标签加粗 */
}

.nickname {
	margin-left: 0; /* 移除偏移 */
	font-size: 26px; /* 字体加大，更明显 */
	color: #000;
	font-weight: bold; /* 昵称内容加粗 */
}

.username-label {
	font-size: 10px; /* 用户名的标签字体变小 */
	color: #888;
}

.username {
	font-size: 14px; /* 用户名的内容字体变小 */
	color: #666;
}
.checkbox-container {
	display: flex;
	justify-content: space-around;
	margin-bottom: 15px;
}

.checkbox-container label {
	font-size: 16px;
	color: #333;
}

</style>
