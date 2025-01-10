<template>
  <view class="hello">
    <!-- 顶部标题和设置按钮 -->
    <view class="header">
      <image 
        src="@/static/icons/arrow-left.svg" 
        alt="上一题" 
        class="icon" 
        @click="goBack" 
      />
      <image 
        src="@/static/icons/shezhi.svg" 
        alt="设置" 
        class="icon" 
        @click="toggleSidebar" 
      />
    </view>

    <!-- 显示问题内容 -->
    <text class="question-title">{{ question.title }}</text>

    <!-- 选项 -->
    <view class="options-group">
      <checkbox-group v-if="question.questionType === '多选题'" @change="onCheckboxChange">
        <label v-for="option in question.options" :key="option.id" class="checkbox-option">
          <checkbox :value="option.optionLabel" />
          {{ option.optionLabel }}. {{ option.optionText }}
        </label>
      </checkbox-group>
      <radio-group v-else @change="onOptionChange">
        <label v-for="option in question.options" :key="option.id" class="radio-option">
          <radio :value="option.optionLabel" :checked="selectedOption === option.optionLabel" />
          {{ option.optionLabel }}. {{ option.optionText }}
        </label>
      </radio-group>
    </view>

    <!-- 分类和答对次数 -->
    <view class="footer">
      <text class="questionType">题型: {{ question.questionType }}</text>
      <text class="correct-count">答对次数: {{ question.correctAnswerCount }}</text> <!-- 显示 correctAnswerCount -->
    </view>

    <!-- 提交答案按钮 -->
    <button @click="submitAnswer" class="confirm-button">确认</button>

    <!-- 答案验证结果 -->
    <view v-if="validationMessage" class="validation-message">
      <text>{{ validationMessage }}</text>
    </view>

    <!-- 显示笔记功能 -->
    <view>
      <textarea 
        v-model="noteContent" 
        class="note-textarea" 
        :maxlength="10000"
        @focus="onFocusNote"
        :class="{'focused': isFocused}"
      ></textarea>
      <button @click="submitNote" class="submit-note-button">提交笔记</button>
    </view>

    <!-- 中间显示的侧边栏 -->
    <view v-if="sidebarVisible" class="sidebar">
      <view class="sidebar-button" @click="goToAdd">手动添加数据</view>
      <view class="sidebar-button" @click="goToFastAdd">一键添加</view>
      <view class="sidebar-button" @click="goToSearchAndEditAndDelete">查询修改与删除</view>
      <view class="sidebar-button" @click="goToFastAddHelp">一键添加示例</view>
      <view class="sidebar-button" @click="goToHome">返回首页</view>
    </view>
  </view>
</template>

<script>
	export default {
	  data() {
	    return {
	      question: {
	        id: null,
	        title: '',
	        questionType: '',
	        category: '',
	        correctChoice: '',
	        correctAnswerCount: 0,  // 使用 correctAnswerCount
	        createdAt: '',
	        createdBy: '',
	        updatedAt: '',
	        updatedBy: '',
	        note: '',
	        wordBook: '',
	        options: [] // 新的数据结构中直接包含选项数组
	      },
	      selectedOption: '', // 单选题用户选中的答案
	      selectedOptions: [], // 多选题用户选中的答案数组
	      validationMessage: '', // 验证结果信息
	      sidebarVisible: false,
	      noteContent: '',
	      isFocused: false
	    };
	  },
	  onLoad() {
	    this.fetchData(); // 页面加载时发起请求
	  },
	  methods: {
	    fetchData() {
	      const token = uni.getStorageSync('token'); // 获取本地存储的 token
	      console.log('Token:', token); // 打印 Token，确保获取正确
	    
	      uni.request({
	        url: 'http://localhost:9192/question/random', // 替换为实际后端接口地址
	        method: 'GET',
	        header: {
	          'token': token, // 使用与 Postman 一致的请求头
	          'Content-Type': 'application/json' // 保留 Content-Type 设置
	        },
	        success: (res) => {
	          if (res.data.code === 1) {
	            this.question = res.data.data; // 更新问题数据
	            console.log("数据是", this.question);
	    
	            // **关键部分：清空选中状态**
	            this.selectedOption = ''; // 重置单选题选项
	            this.selectedOptions = []; // 重置多选题选项
	    
	            // 初始化笔记
	            this.noteContent = this.question.note || ''; 
	          } else {
	            uni.showToast({
	              title: res.data.msg || '加载失败，请稍后重试',
	              icon: 'none',
	              duration: 2000
	            });
	          }
	        },
	        fail: () => {
	          uni.showToast({
	            title: '请求失败，请检查后端接口。',
	            icon: 'none',
	            duration: 2000
	          });
	        }
	      });
	    },
	    onOptionChange(event) {
	      this.selectedOption = event.detail.value;
	    },
	    onCheckboxChange(event) {
	      this.selectedOptions = event.detail.value;
	    },
	    submitAnswer() {
	      const correctAnswers = this.question.correctChoice
	        ? this.question.correctChoice.split(',').map(a => a.trim().toUpperCase())
	        : [];
	      let isCorrect = false;
	
	      if (this.question.questionType === '单选题') {
	        const userAnswer = this.selectedOption.trim().toUpperCase();
	        isCorrect = userAnswer === correctAnswers[0];
	        this.validationMessage = isCorrect
	          ? '恭喜你，回答正确！'
	          : `很遗憾，回答错误！正确答案是：${correctAnswers[0]}`;
	      } else {
	        const userAnswers = this.selectedOptions.map(a => a.trim().toUpperCase());
	        isCorrect = correctAnswers.length === userAnswers.length && correctAnswers.every(ans => userAnswers.includes(ans));
	        this.validationMessage = isCorrect
	          ? '恭喜你，回答正确！'
	          : `很遗憾，回答错误！正确答案是：${correctAnswers.join(', ')}`;
	      }
	
	      // 更新数据库
	      const token = uni.getStorageSync('token');
	      const updateData = {
	        id: this.question.id,
	        correctAnswerCount: isCorrect ? this.question.correctAnswerCount + 1 : this.question.correctAnswerCount,
	        totalAnswerCount: this.question.totalAnswerCount + 1
	      };
	
	      uni.request({
	        url: 'http://localhost:9192/question/updateUserQuestionStats',
	        method: 'PUT',
	        header: {
	          'token': token,
	          'Content-Type': 'application/json'
	        },
	        data: updateData,
	        success: (res) => {
	          if (res.data.code === 1) {
	            console.log('数据已更新');
	          } else {
	            uni.showToast({
	              title: '更新失败，请稍后重试',
	              icon: 'none',
	              duration: 2000
	            });
	          }
	        },
	        fail: () => {
	          uni.showToast({
	            title: '请求失败，请检查后端接口。',
	            icon: 'none',
	            duration: 2000
	          });
	        }
	      });
	
	      // 如果答对了才刷新页面
	      if (isCorrect) {
	        setTimeout(() => {
	          this.fetchData(); // 加载新问题
	          this.validationMessage = '';
	        }, 1000);
	      }
	    },
	    toggleSidebar() {
	      this.sidebarVisible = !this.sidebarVisible;
	    },
	    submitNote() {
	      const token = uni.getStorageSync('token'); // 获取本地存储的 token
	      uni.request({
	        url: 'http://localhost:9192/question/updateNote',
	        method: 'PUT',
	        header: {
	          'token': token, // 使用与 Postman 一致的请求头
	        },
	        data: { id: this.question.id, note: this.noteContent },
	        success: (res) => {
	          if (res.data.code === 1) {
	            uni.showToast({ title: '笔记已提交', icon: 'success', duration: 2000 });
	          } else {
	            uni.showToast({ title: '提交失败，请稍后重试', icon: 'none', duration: 2000 });
	          }
	        },
	        fail: () => {
	          uni.showToast({ title: '请求失败，请检查后端接口。', icon: 'none', duration: 2000 });
	        }
	      });
	    },
		goToAdd(){
			this.sidebarVisible = false;
			uni.navigateTo({ url: '/pages/addPage/addPage' });
		},
		goToSearchAndEditAndDelete(){
			this.sidebarVisible = false;
			uni.navigateTo({
				url:"/pages/searchAndEditAndDelete/searchAndEditAndDelete"
			})
		},
		goToFastAdd(){
			this.sidebarVisible = false;
			uni.navigateTo({
				url:"/pages/fastAddPage/fastAddPage"
			})
		},
		goToFastAddHelp() {
		  this.sidebarVisible = false;
		  uni.navigateTo({ url: '/pages/fastAddPageHelp/fastAddPageHelp' });
		},
		
		goToHome() {
		  this.sidebarVisible = false;
		  uni.switchTab({ url: '/pages/index/index' });
		},
		
	  }
	};

</script>
<style scoped>
/* 样式与之前保持一致 */
view {
  user-select: text; /* 允许文字选中 */
}
.hello {
  padding: 20px;
  font-family: 'Arial', sans-serif;
  background-color: #f5f5f5;
  height: 100vh;
  box-sizing: border-box;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.icon {
  height: 25px;
  width: 25px;
  cursor: pointer;
}

.question-title {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.options-group {
  margin-bottom: 20px;
}

.radio-option, .checkbox-option {
  font-size: 16px;
  color: #555;
  display: flex;
  align-items: center;
  gap: 8px;
}

.footer {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.confirm-button {
  background-color: #007AFF;
  color: white;
  padding: 8px 16px;
  border-radius: 5px;
  font-size: 16px;
  border: none;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 122, 255, 0.3);
  transition: background-color 0.3s, transform 0.2s;
}

.confirm-button:hover {
  background-color: #005bbb;
  transform: translateY(-2px);
}

.validation-message {
  margin-top: 20px;
  color: green;
  font-size: 16px;
  font-weight: bold;
}

.sidebar {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.sidebar-button {
  background-color: #007AFF;
  color: white;
  margin-bottom: 10px;
  padding: 10px 20px;
  border-radius: 5px;
  text-align: center;
}
.note-textarea {
  width: 100%;
  height: 120px;
  margin-top: 20px;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: none;
}

.note-textarea.focused {
  border-color: #007AFF;
}

.submit-note-button {
  background-color: #007AFF;
  color: white;
  margin-top: 10px;
  padding: 10px 20px;
  border-radius: 5px;
}


</style>
