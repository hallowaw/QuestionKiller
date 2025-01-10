<template>
  <view class="container">
    <!-- 搜索框 -->
    <view class="search-bar">
      <input
        v-model="query.title"
        placeholder="请输入标题关键词"
        class="search-input"
        @confirm="search"
      />
      <button @click="search" class="search-button">搜索</button>
    </view>

    <!-- 查询结果列表 -->
    <view v-if="records.length > 0" class="result-list">
      <view v-for="(item, index) in records" :key="index" class="result-item">
        <text class="item-index">{{ (currentPage - 1) * query.pageSize + index + 1 }}</text>
        <text @click="showDetail(item)" class="item-title">{{ item.title }}</text>
        <!-- 修改按钮 -->
        <button @click="editItem(item)" class="item-button" :disabled="permission === 'READ'">修改</button>
        <!-- 删除按钮 -->
        <button @click="deleteItem(item.id)" class="item-button" :disabled="permission === 'READ'">删除</button>
        
      </view>
    </view>
    <view v-else class="no-data">暂无数据</view>

    <!-- 分页栏 -->
    <view v-if="total > 0" class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <view v-for="page in totalPages" :key="page" @click="changePage(page)" class="page-item">
        <text :class="{ active: currentPage === page }">{{ page }}</text>
      </view>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
    </view>

    <!-- 详情弹窗 -->
    <view v-if="detailVisible" class="detail-popup">
      <view class="detail-content">
        <text>标题: {{ detailData.title }}</text>
        <text>题型: {{ detailData.questionType }}</text>
        <text>分类: {{ detailData.category }}</text>
        <text>正确答案: {{ detailData.correctChoice }}</text>
        <view v-for="(option, index) in detailData.options" :key="index">
          <text>{{ option.optionLabel }}: {{ option.optionText }}</text>
        </view>
        <button @click="detailVisible = false" class="close-button">关闭</button>
      </view>
    </view>

    <!-- 修改表单弹窗 -->
    <view v-if="editVisible" class="edit-popup">
      <view class="edit-content">
        <text>标题:</text>
        <input v-model="editForm.title" placeholder="请输入标题" class="custom-input" />

        <view v-for="(option, index) in editForm.options" :key="index">
          <text>选项{{ option.optionLabel }}:</text>
          <input
            v-model="editForm.options[index].optionText"
            :placeholder="'请输入选项' + option.optionLabel"
            class="custom-input"
          />
        </view>

        <text>题型:</text>
        <select v-model="editForm.questionType" @change="handleQuestionTypeChange">
          <option value="单选题">单选题</option>
          <option value="多选题">多选题</option>
        </select>

        <text>正确答案:</text>
        <checkbox-group @change="handleCheckboxChange">
          <checkbox
            v-for="(option, index) in editForm.options"
            :key="index"
            :value="option.optionLabel"
            :checked="selectedChoices.includes(option.optionLabel)"
          >
            {{ option.optionLabel }}
          </checkbox>
        </checkbox-group>

        <text>分类:</text>
        <input v-model="editForm.category" placeholder="请输入分类" class="custom-input" />

        <text>笔记:</text>
        <input v-model="editForm.note" placeholder="请输入笔记" class="custom-input" />

        <text>状态:</text>
        <select v-model="editForm.status">
          <option value="0">禁用</option>
          <option value="1">启用</option>
        </select>

        <button @click="updateItem" class="update-button">确认修改</button>
        <button @click="clearForm" class="clear-button">清空</button>
        <button @click="editVisible = false" class="close-button">关闭</button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      query: {
        title: '',
        page: 1,
        pageSize: 10,
        wordBookId: 1,
      },
      total: 0,
      records: [],
      currentPage: 1,
      totalPages: 1,
	  permission: 'READ', 
      detailVisible: false,
      detailData: {},
      editVisible: false,
      editForm: {
        id: null,
        title: '',
        questionType: '单选题',
        category: '',
        note: '',
        status: '1',
        correctChoice: '',
        options: [
          { optionLabel: 'A', optionText: '' },
          { optionLabel: 'B', optionText: '' },
          { optionLabel: 'C', optionText: '' },
          { optionLabel: 'D', optionText: '' },
        ],
      },
      selectedChoices: [],
    };
  },
  methods: {
    search() {
      this.query.page = 1;
      this.getData();
    },
  getPermission() {
	uni.request({
	  url: 'http://localhost:9192/user/getPermission',
	  method: 'GET',
	  header: {
		token: uni.getStorageSync("token"),
	  },
	  success: (res) => {
		if (res.data.code === 1) {
		  this.permission = res.data.data; // 设置权限
		} else {
		  uni.showToast({ title: '权限加载失败', icon: 'none' });
		}
	  },
	  fail: () => {
		uni.showToast({ title: '网络错误', icon: 'none' });
	  },
	});
  },
	changePage(page) {
	    if (page < 1 || page > this.totalPages) return; // 防止页码越界
	    this.currentPage = page;
	    this.query.page = page;  // 更新当前页码
	    this.getData();  // 重新加载数据
	  },
	deleteItem(id) {
	  const self = this;
	  uni.showModal({
	    title: '删除确认',
	    content: '确定要删除此题目吗？',
	    success: function (res) {
	      if (res.confirm) {
	        uni.request({
	          url: `http://localhost:9192/question/delete/${id}`,
	          method: 'DELETE',
	          header: {
	            token: uni.getStorageSync("token"),
	          },
	          success: function (response) {
	            if (response.data.code === 1) {
	              uni.showToast({ title: '删除成功', icon: 'success' });
	              self.getData();  // 删除后重新加载数据
	            } else {
	              uni.showToast({ title: '删除失败', icon: 'none' });
	            }
	          },
	          fail: function () {
	            uni.showToast({ title: '网络错误', icon: 'none' });
	          }
	        });
	      }
	    }
	  });
	},
	getWordBookId() {
	  uni.request({
	    url: 'http://localhost:9192/user/getDefaultWordBookIdByCurrentID',
	    method: 'GET',
	    header: {
	      token: uni.getStorageSync("token"),
	    },
	    success: (res) => {
	      if (res.data.code === 1) {
	        this.query.wordBookId = res.data.data; // 设置 wordBookId
	        this.getData(); // 在获取到 wordBookId 后加载数据
	      } else {
	        uni.showToast({ title: '加载失败', icon: 'none' });
	      }
	    },
	    fail: () => {
	      uni.showToast({ title: '网络错误', icon: 'none' });
	    },
	  });
	},
	 clearForm() {
	      this.editForm = {
	        id: null,
	        title: '',
	        questionType: '单选题',
	        category: '',
	        note: '',
	        status: '1',
	        correctChoice: '',
	        options: [
	          { optionLabel: 'A', optionText: '' },
	          { optionLabel: 'B', optionText: '' },
	          { optionLabel: 'C', optionText: '' },
	          { optionLabel: 'D', optionText: '' },
	        ],
	      };
	      this.selectedChoices = [];
	    },
    getData() {
      uni.request({
        url: `http://localhost:9192/question/page`,
        method: 'GET',
        header: {
          token: uni.getStorageSync("token"),
        },
        data: this.query,
        success: (res) => {
          if (res.data.code === 1) {
            const result = res.data.data;
            this.records = result.records;
            this.total = result.total;
            this.totalPages = Math.ceil(this.total / this.query.pageSize);
          }
        },
      });
    },
    showDetail(item) {
      uni.request({
        url: `http://localhost:9192/question/getQuestion/${item.id}`,
        method: 'GET',
        header: {
          token: uni.getStorageSync("token"),
        },
        success: (res) => {
          if (res.data.code === 1) {
            this.detailData = res.data.data;
            this.detailVisible = true;
          }
        },
      });
    },
    editItem(item) {
      uni.request({
        url: `http://localhost:9192/question/getQuestion/${item.id}`,
        method: 'GET',
        header: {
          token: uni.getStorageSync("token"),
        },
        success: (res) => {
          if (res.data.code === 1) {
            const data = res.data.data;
            this.editForm = {
              id: data.id,
              title: data.title,
              questionType: data.questionType,
              category: data.category,
              note: data.note,
              status: data.status,
              correctChoice: data.correctChoice,
              options: data.options || [
                { optionLabel: 'A', optionText: '' },
                { optionLabel: 'B', optionText: '' },
                { optionLabel: 'C', optionText: '' },
                { optionLabel: 'D', optionText: '' },
              ],
            };
            this.selectedChoices = data.correctChoice.split(',');
            this.editVisible = true;
          }
        },
      });
    },

handleQuestionTypeChange() {
    if (this.editForm.questionType === '单选题') {
      // 初始化单选题答案
      this.editForm.correctChoice = this.editForm.options[0]?.optionLabel || '';
    } else if (this.editForm.questionType === '多选题') {
      // 初始化多选题答案
      this.selectedChoices = [];
      this.editForm.correctChoice = '';
    }
  },
  handleCheckboxChange(event) {
    this.selectedChoices = event.detail.value;
    this.editForm.correctChoice = this.selectedChoices.join(',');
  },
    updateItem() {
      const type = this.editForm.questionType;

      // 校验逻辑
      if (type === '单选题' && this.selectedChoices.length !== 1) {
        return uni.showModal({ title: '提示', content: '单选题只能选择一个答案' });
      }
      if (type === '多选题' && this.selectedChoices.length < 2) {
        return uni.showModal({ title: '提示', content: '多选题至少选择两个答案' });
      }

      this.editForm.correctChoice = this.selectedChoices.join(',');
        // 提交更新请求
        uni.request({
          url: 'http://localhost:9192/question/update',
          method: 'PUT',
          header: {
            token: uni.getStorageSync("token"),
          },
          data: this.editForm,
          success: (res) => {
            if (res.data.code === 1) {
              uni.showToast({ title: '修改成功', icon: 'success' });
              this.editVisible = false;
              this.getData();
            }
          },
        });
      },
    },
	


   
  
mounted() {
  this.getWordBookId(); // 在页面加载时调用
   this.getPermission(); // 获取权限
},
};
</script>

<style>


.custom-input {
  width: 100%; /* 占满父级宽度 */

  margin-bottom: 10rpx; /* 每个输入框间增加间距 */
  border: 1px solid #ccc; /* 边框颜色 */
  border-radius: 5rpx; /* 圆角边框 */
  font-size: 28rpx; /* 调整字体大小 */
}

.container {
  padding: 20rpx;
}
.edit-popup, .detail-popup {
  z-index: 500 !important; /* 这里设置为 500，确保低于系统的 showModal */
}
.item-index {
  margin-right: 10rpx;
  font-weight: bold;
}
.search-bar {
  display: flex;
  margin-bottom: 20rpx;
}
.search-input {
  flex: 1;
  padding: 10rpx;
  border: 1px solid #ccc;
}

.search-button {
  padding: 10rpx;
  background-color: #007aff;
  color: white;
}
.result-list {
  margin-bottom: 20rpx;
}
.result-item {
  display: flex;
  align-items: center;
  padding: 10rpx;
  border: 1px solid #ccc;
  margin-bottom: 10rpx;
}
.item-checkbox {
  margin-right: 10rpx;
}
.item-title {
  flex: 1;
  cursor: pointer;
  color: #007aff;
}
.item-button {
  margin-left: 10rpx;
  padding: 5rpx 10rpx;
}
.no-data {
  text-align: center;
  color: #ccc;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
}
.page-item {
  margin: 0 5rpx;
  cursor: pointer;
}
.page-item .active {
  color: #007aff;
}
.detail-popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.detail-content {
  background: white;
  padding: 20rpx;
  border-radius: 10rpx;
  width: 80%;
}
.edit-popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.edit-content {
  background: white;
  padding: 20rpx;
  border-radius: 10rpx;
  width: 80%;
}
.update-button,
.clear-button,
.close-button {
  margin-top: 10rpx;
  padding: 10rpx;
  width: 100%;
  background-color: #007aff;
  color: white;
  text-align: center;
}
</style>