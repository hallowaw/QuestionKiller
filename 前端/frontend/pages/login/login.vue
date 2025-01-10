<template>
  <view class="login-container">
    <!-- 标题 -->
    <view class="title-container">
      <text class="title">智能学习辅助系统</text>
    </view>

    <!-- 用户名输入框 -->
    <view class="form-item">
      <view class="svg-container">
        <uni-icons type="person" size="20" color="#889aa4" />
      </view>
      <input
        v-model="form.username"
        class="input"
        placeholder="请输入用户名"
        type="text"
      />
	    <view class="show-pwd"></view>
    </view>

    <!-- 密码输入框 -->
    <view class="form-item">
      <view class="svg-container">
        <uni-icons type="locked" size="20" color="#889aa4" />
      </view>
      <input
        v-model="form.password"
        class="input"
        :type="passwordType"
        placeholder="请输入密码"
      />
      <view class="show-pwd" @tap="togglePwd">
        <uni-icons
          :type="passwordType === 'password' ? 'eye' : 'eye-filled'"
          size="20"
          color="#889aa4"
        />
      </view>
    </view>

    <!-- 登录按钮 -->
    <button class="login-btn" :loading="loading" @tap="handleSubmit">
      登录
    </button>

    <!-- 底部链接 -->
    <view class="link-container">
      <text  class="link-text" @tap="showRegister">注册</text>
      <text  class="link-text" @tap="showChangePassword" style="margin-left: 10px;">修改密码</text>
    </view>

    <!-- 注册弹窗 -->
    <view v-if="showRegisterPopup" class="popup-overlay">
      <view class="popup-container">
        <view class="popup-title">注册</view>

        <!-- 用户名输入框 -->
        <input v-model="registerForm.username" placeholder="用户名" class="popup-input" />

        <!-- 密码输入框 -->
        <input v-model="registerForm.password" placeholder="密码" class="popup-input" type="password" />

        <!-- 邮箱输入框 -->
        <input v-model="registerForm.email" placeholder="邮箱" class="popup-input" type="email" />

        <!-- 注册按钮 -->
        <button class="register-btn" @tap="handleRegister">注册</button>

        <!-- 关闭按钮 -->
        <view class="close-btn" @tap="closeRegister">×</view>
      </view>
    </view>

    <!-- 修改密码弹窗 -->
    <view v-if="showChangePasswordPopup" class="popup-overlay">
      <view class="popup-container">
        <view class="popup-title">修改密码</view>

        <!-- 用户名输入框 -->
        <input v-model="changePasswordForm.username" placeholder="用户名" class="popup-input" />

        <!-- 旧密码输入框 -->
        <input v-model="changePasswordForm.oldPassword" placeholder="旧密码" class="popup-input" type="password" />

        <!-- 新密码输入框 -->
        <input v-model="changePasswordForm.newPassword" placeholder="新密码" class="popup-input" type="password" />

        <!-- 修改密码按钮 -->
        <button class="register-btn" @tap="handleChangePassword">修改密码</button>

        <!-- 关闭按钮 -->
        <view class="close-btn" @tap="closeChangePassword">×</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      passwordType: 'password',
      loading: false,
      showRegisterPopup: false, // 控制注册弹窗显示
      showChangePasswordPopup: false, // 控制修改密码弹窗显示
      registerForm: {
        username: '',
        password: '',
        email: ''
      },
      changePasswordForm: {
        username: '',
        oldPassword: '',
        newPassword: ''
      }
    };
  },
  methods: {
    // 切换密码显示隐藏
    togglePwd() {
      this.passwordType =
        this.passwordType === 'password' ? 'text' : 'password';
    },

    // 表单提交处理
    handleSubmit() {
      if (!this.form.username || !this.form.password) {
        uni.showToast({ title: '用户名和密码不能为空', icon: 'none' });
        return;
      }

      this.loading = true;
      uni.request({
        url: 'http://localhost:9192/user/login',
        method: 'POST',
        data: {
          username: this.form.username,
          password: this.form.password
        },
        success: (res) => {
          if (res.data.code === 1) {
            uni.showToast({
              title: '登录成功',
              icon: 'success'
            });
            uni.setStorageSync('token', res.data.data);
            uni.switchTab({ url: '/pages/index/index' });
          } else {
            uni.showToast({ title: res.data.msg || '登录失败', icon: 'none' });
          }
        },
        fail: () => {
          uni.showToast({ title: '网络错误，请稍后重试', icon: 'none' });
        },
        complete: () => {
          this.loading = false;
        }
      });
    },

    // 显示注册弹窗
    showRegister() {
      this.showRegisterPopup = true;
    },

    // 关闭注册弹窗
    closeRegister() {
      this.showRegisterPopup = false;
    },

    // 注册处理
    handleRegister() {
      const { username, password, email } = this.registerForm;

      if (!username || !password || !email) {
        uni.showToast({ title: '所有字段都是必填项', icon: 'none' });
        return;
      }

      uni.request({
        url: 'http://localhost:9192/user/register',
        method: 'POST',
        data: {
          username,
          password,
          email
        },
        success: (res) => {
          if (res.data.code === 1) {
            uni.showToast({ title: '注册成功', icon: 'success' });
            this.closeRegister();
          } else {
            uni.showToast({ title: res.data.msg || '注册失败', icon: 'none' });
          }
        },
        fail: () => {
          uni.showToast({ title: '网络错误，请稍后重试', icon: 'none' });
        }
      });
    },

    // 显示修改密码弹窗
    showChangePassword() {
      this.showChangePasswordPopup = true;
    },

    // 关闭修改密码弹窗
    closeChangePassword() {
      this.showChangePasswordPopup = false;
    },

    // 修改密码处理
    handleChangePassword() {
      const { username, oldPassword, newPassword } = this.changePasswordForm;

      if (!username || !oldPassword || !newPassword) {
        uni.showToast({ title: '所有字段都是必填项', icon: 'none' });
        return;
      }

      uni.request({
        url: 'http://localhost:9192/user/updatePassword',
        method: 'PUT',
        data: {
          username,
          oldPassword,
          newPassword
        },
        success: (res) => {
          if (res.data.code === 1) {
            uni.showToast({ title: '密码修改成功', icon: 'success' });
            this.closeChangePassword();
          } else {
            uni.showToast({ title: res.data.msg || '密码修改失败', icon: 'none' });
          }
        },
        fail: () => {
          uni.showToast({ title: '网络错误，请稍后重试', icon: 'none' });
        }
      });
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}
.title-container {
  margin-bottom: 20px;
}
.title {
  font-size: 24px;
  font-weight: bold;
}
.form-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  width: 100%;
}
.input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-left: 10px;
}
.login-btn {
  width: 100%;
  background-color: #007aff;
  color: white;
  padding: 10px;
  text-align: center;
  border-radius: 4px;
}
.show-pwd {
  margin-left: 10px;
}
.link-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-top: 10px;
}
.link-text {
  color: #007aff;
  font-size: 14px;
  cursor: pointer;
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
}
.popup-container {
  width: 80%;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  position: relative;
}
.popup-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
}
.popup-input {
  width: 95%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 15px;
}
.register-btn {
  width: 100%;
  background-color: #28a745;
  color: white;
  padding: 10px;
  text-align: center;
  border-radius: 4px;
}
.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 18px;
  cursor: pointer;
}
</style>
