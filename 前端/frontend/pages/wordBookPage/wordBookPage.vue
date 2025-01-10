<template>
  <view class="container">
    <!-- 词书列表 -->
    <view class="wordbook-item" v-for="item in wordBooks" :key="item.id">
      <text class="id">{{ item.id }}</text>
      <text class="title" @click="showDescription(item.description)">{{ truncate(item.name) }}</text>
      <text class="desc">{{ truncate(item.description) }}</text>

      <button class="btn default-btn" 
              :class="{ active: item.id === defaultWordBookId }" 
              @click="setDefault(item.id)">
        {{ item.id === defaultWordBookId ? '默认' : '设为默认' }}
      </button>

      <button class="btn edit-btn" @click="openEditPopup(item)">修改</button>
	<button 
	  class="btn delete-btn" 
	  :class="{ disabled: item.id === defaultWordBookId }"
	  :disabled="item.id === defaultWordBookId"
	  @click="confirmDelete(item.id)">
	  删除
	</button>
    </view>
	
<!-- 弹窗：修改词书 -->
<view v-if="showEditPopup" class="popup-overlay">
  <view class="popup-container">
    <view class="popup-title">修改词书</view>
    <!-- 输入框 -->
    <input v-model="editItem.name" placeholder="请输入名称" class="popup-input" maxlength="10" />
    <input v-model="editItem.description" placeholder="请输入描述" class="popup-input" maxlength="10" />

    <!-- 新增勾选框 -->
    <view class="checkbox-group">
<checkbox-group>
  <checkbox :value="1" 
            :checked="editItem.isPublic === 1" 
            @tap="toggleCheckbox(1)">
    <text class="checkbox-label">公开</text>
  </checkbox>
  <checkbox :value="0" 
            :checked="editItem.isPublic === 0" 
            @tap="toggleCheckbox(0)">
    <text class="checkbox-label">私密</text>
  </checkbox>
</checkbox-group>
    </view>

    <button class="register-btn" @click="updateWordBook">确认修改</button>
    <view class="close-btn" @click="closeEditPopup">×</view>
  </view>
</view>

    <!-- 弹窗：描述展示 -->
    <view v-if="showDescPopup" class="popup-overlay">
      <view class="popup-container">
        <view class="popup-title">词书描述</view>
        <text>{{ currentDesc }}</text>
        <button class="register-btn" @click="closeDescPopup">关闭</button>
      </view>
    </view>
  </view>
</template>

<script>
import { onLoad } from '@dcloudio/uni-app'

export default {
  data() {
    return {
      // 新增字段 isPublic
      wordBooks: [],
      defaultWordBookId: null,
      editItem: {
        id: null,
        name: '',
        description: '',
        isPublic: 0, // 默认值
      },
      currentDesc: '',
      showEditPopup: false,
      showDescPopup: false,
      token: uni.getStorageSync('token'), // 从缓存获取token
    }
  },
  methods: {
    // 查询词书列表
    async fetchWordBooks() {
      const res = await uni.request({
        url: 'http://localhost:9192/wordBook/getWordBookByUserId',
        method: 'GET',
        header: { token: this.token },
      })
      if (res.data.code === 1) {
        this.wordBooks = res.data.data
      }
    },

    // 查询默认词书
    async fetchDefaultWordBook() {
      const res = await uni.request({
        url: 'http://localhost:9192/user/getDefaultWordBookIdByCurrentID',
        method: 'GET',
header: { token: this.token },
      })
      if (res.data.code === 1) {
        this.defaultWordBookId = res.data.data
      }
    },

    // 设置默认词书
    async setDefault(id) {
      const res = await uni.request({
        url: `http://localhost:9192/user/updateDefaultWordBookId?wordBookId=${id}`,
        method: 'PUT',
header: { token: this.token },
      })
      if (res.data.code === 1) {
        this.defaultWordBookId = id
        uni.showToast({ title: '设置成功', icon: 'success' })
      }
    },

    // 修改词书
    openEditPopup(item) {
      this.editItem = { ...item } // 将item的数据复制到editItem
      this.showEditPopup = true
    },
	
toggleCheckbox(value) {
  if (this.editItem.isPublic !== value) {
    this.editItem.isPublic = value // 确保只能选一个
  }
},

   async updateWordBook() {
         const res = await uni.request({
           url: 'http://localhost:9192/wordBook/update',
           method: 'PUT',
           header: { token: this.token },
           data: {
             id: this.editItem.id,
             name: this.editItem.name,
             description: this.editItem.description,
             isPublic: this.editItem.isPublic, // 提交勾选结果
           },
         })
         if (res.data.code === 1) {
           uni.showToast({ title: '修改成功', icon: 'success' })
           this.showEditPopup = false
           this.fetchWordBooks()
         }
       },
     

    // 删除词书
    async confirmDelete(id) {
      const res = await uni.request({
        url: `http://localhost:9192/wordBook/delete/${id}`,
        method: 'DELETE',
	header: { token: this.token },
      })
      if (res.data.code === 1) {
        uni.showToast({ title: '删除成功', icon: 'success' })
        this.fetchWordBooks()
      }
    },

    // 描述弹窗
    showDescription(description) {
      this.currentDesc = description
      this.showDescPopup = true
    },

    // 关闭弹窗
    closeEditPopup() {
      this.showEditPopup = false
    },
    closeDescPopup() {
      this.showDescPopup = false
    },

    // 文本截断
    truncate(text) {
      return text.length > 10 ? text.substring(0, 10) + '...' : text
    },
  },

  onLoad() {
    this.fetchWordBooks()
    this.fetchDefaultWordBook()
  },
}
</script>
<style>
	.container {
	  padding: 20rpx;
	}
	
	.wordbook-item {
	  display: flex;
	  align-items: center;
	  margin-bottom: 20rpx;
	  padding: 10rpx;
	  border-bottom: 1px solid #eee;
	}
	
	.id {
	  width: 50rpx;
	  font-weight: bold;
	}
	
	.title {
	  flex: 1;
	  padding-left: 10rpx;
	  color: #007aff;
	}
	
	.desc {
	  flex: 2;
	  padding-left: 10rpx;
	  color: #666;
	}
	
	.btn {
	  margin-left: 10rpx;
	  padding: 5rpx 10rpx;
	  font-size: 12px;
	  border-radius: 5rpx;
	}
	
	.default-btn {
	  background-color: #f5f5f5;
	}
	
	.default-btn.active {
	  background-color: #007aff;
	  color: white;
	}
	
	.edit-btn {
	  background-color: #ffc107;
	  color: white;
	}
	
	.delete-btn {
	  background-color: #ff4d4f;
	  color: white;
	}
	
	/* 弹窗样式 */
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
	  background-color: #fff;
	  padding: 20px;
	  border-radius: 10px;
	  width: 80%;
	  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
	  text-align: center;
	}
	
	.popup-title {
	  font-size: 18px;
	  font-weight: bold;
	  margin-bottom: 10px;
	}
	
	.popup-input {
	  width: 100%;
	  padding: 10px;
	  margin: 10px 0;
	  border: 1px solid #ccc;
	  border-radius: 5px;
	}
	
	.register-btn {
	  width: 100%;
	  padding: 10px;
	  margin: 10px 0;
	  background-color: #007aff;
	  color: white;
	  border: none;
	  border-radius: 5px;
	}
	
	.close-btn {
	  position: absolute;
	  top: 10px;
	  right: 15px;
	  font-size: 20px;
	  cursor: pointer;
	}
	.delete-btn.disabled {
	  background-color: #ddd;  /* 灰色背景 */
	  color: #aaa;  /* 灰色字体 */
	}
	.delete-btn[disabled] {
	  cursor: not-allowed;
	}
</style>