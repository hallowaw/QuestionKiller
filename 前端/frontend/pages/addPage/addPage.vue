<template>
  <div class="add-question-form">
    <!-- 标题输入框 -->
    <div class="form-group">
      <label for="title">标题</label>
      <input
        v-model="title"
        id="title"
        type="text"
        maxlength="50"
        :class="{ 'error-border': title.length > 50 }"
        @input="validateTitle"
      />
      <p v-if="title.length > 50" class="error-text">标题不能超过 50 字符</p>
    </div>

    <!-- 题型选择框 -->
    <div class="form-group">
      <label for="questionType">题型</label>
      <select v-model="questionType" id="questionType" @change="resetCorrectAnswers">
        <option value="单选题">单选题</option>
        <option value="多选题">多选题</option>
      </select>
    </div>

    <!-- 分类输入框 -->
    <div class="form-group">
      <label for="category">分类</label>
      <input
        v-model="category"
        id="category"
        type="text"
        placeholder="默认分类"
      />
    </div>

    <!-- 备注输入框 -->
    <div class="form-group">
      <label for="note">备注</label>
      <textarea
        v-model="note"
        id="note"
        rows="3"
      ></textarea>
    </div>

    <!-- 选项动态管理区 -->
    <div class="form-group">
      <label>选项</label>
      <div v-for="(option, index) in options" :key="index" class="option-item">
        <!-- 标签 -->
        <span class="option-label">{{ String.fromCharCode(65 + index) }}.</span>

        <!-- 输入选项 -->
        <input
          v-model="option.optionText"
          type="text"
          placeholder="选项文字"
        />

        <!-- 删除按钮 -->
        <button class="delete-btn" @click="removeOption(index)">删除</button>
      </div>

      <!-- 添加选项按钮 -->
      <p v-if="options.length >= 10" class="error-text">最多只能添加 10 个选项</p>
      <button
        class="add-btn"
        @click="addOption"
        :disabled="options.length >= 10"
      >
        增加选项
      </button>
    </div>

    <!-- 正确答案区 -->
    <div class="form-group">
      <label>正确答案区</label>

      <!-- 单选题 -->
      <div v-if="questionType === '单选题'">
        <select v-model="correctChoiceSingle">
          <option v-for="(option, index) in options" :key="index" :value="String.fromCharCode(65 + index)">
            {{ String.fromCharCode(65 + index) }}
          </option>
        </select>
      </div>

      <!-- 多选题 -->
      <div v-else-if="questionType === '多选题'">
        <checkbox-group @change="onMultipleChoiceChange">
          <label v-for="(option, index) in options" :key="index" class="checkbox-item">
            <checkbox 
              :value="String.fromCharCode(65 + index)" 
              :checked="correctChoiceMulti.includes(String.fromCharCode(65 + index))" 
            />
            {{ String.fromCharCode(65 + index) }}
          </label>
        </checkbox-group>
      </div>
    </div>

    <!-- 关联词书选择区 -->
    <div class="form-group">
      <label for="wordBook">关联词书</label>
      <select v-model="selectedWordBookId" id="wordBook">
        <option
          v-for="wordBook in wordBooks"
          :key="wordBook.id"
          :value="wordBook.id"
        >
          {{ wordBook.name }}
        </option>
      </select>
    </div>

    <!-- 提交与重置按钮 -->
    <div class="form-group buttons">
      <button class="submit-btn" @click="submitForm">提交</button>
      <button class="reset-btn" @click="resetForm">重置</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      title: "",
      questionType: "单选题",
      category: "默认分类",
      note: "",
      options: [
        { optionText: "" },
        { optionText: "" }
      ],
      correctChoiceSingle: "A", // 单选题默认答案
      correctChoiceMulti: [],   // 多选题默认答案
      selectedWordBookId: null,
      wordBooks: [],
      token: uni.getStorageSync("token"), // 从缓存获取token
    };
  },
  methods: {
    // 验证标题
    validateTitle() {
      if (this.title.length > 50) {
        console.warn("标题超过最大长度");
      }
    },

    // 添加选项
    addOption() {
      if (this.options.length < 10) {
        this.options.push({ optionText: "" });
      }
    },
onMultipleChoiceChange(e) {
  this.correctChoiceMulti = e.detail.value; // 更新选择的答案数组
},

    // 删除选项
    removeOption(index) {
      if (this.options.length > 2) {
        this.options.splice(index, 1);
      } else {
        alert("至少需要两个选项！");
      }
      this.resetCorrectAnswers(); // 删除后重置答案
    },

    // 重置正确答案
    resetCorrectAnswers() {
      this.correctChoiceSingle = "A";
      this.correctChoiceMulti = [];
    },

    // 重置表单
    resetForm() {
      this.title = "";
      this.questionType = "单选题";
      this.category = "默认分类";
      this.note = "";
      this.options = [
        { optionText: "" },
        { optionText: "" }
      ];
      this.correctChoiceSingle = "A";
      this.correctChoiceMulti = [];
      this.selectedWordBookId =
        this.wordBooks.length > 0 ? this.wordBooks[0].id : null;
    },

    // 加载词书
    fetchWordBooks() {
      uni.request({
        url: "http://localhost:9192/user/getDefaultWordBookIdByCurrentID",
        method: "GET",
        header: { token: this.token },
        success: (defaultRes) => {
          const defaultId = defaultRes.data.data;
          uni.request({
            url: "http://localhost:9192/wordBook/getWordBookByUserId",
            method: "GET",
            header: { token: this.token },
            success: (response) => {
              this.wordBooks = response.data.data;
              this.selectedWordBookId = defaultId;
            },
          });
        },
      });
    },

    // 提交表单
    submitForm() {
      // 校验标题
      if (!this.title) {
        uni.showToast({ title: "标题不能为空！", icon: "none" });
        return;
      }
    
      // 验证选项是否为空
      if (this.options.some((o) => !o.optionText.trim())) {
        uni.showToast({ title: "选项内容不能为空！", icon: "none" });
        return;
      }
    
      // 验证正确答案
      let correctChoice = "";
      if (this.questionType === "单选题") {
        if (!this.correctChoiceSingle) {
          uni.showToast({ title: "请选择正确答案！", icon: "none" });
          return;
        }
        correctChoice = this.correctChoiceSingle;
      } 
	  else if (this.questionType === "多选题") {
	    if (!Array.isArray(this.correctChoiceMulti) || this.correctChoiceMulti.length < 2) {
	      uni.showToast({ title: "多选题至少需要两个正确答案！！", icon: "none" });
	      return;
	    }
	  
    
    
    
        correctChoice = this.correctChoiceMulti.join(","); // 转换为字符串
      }
    
      // 提交请求数据
      const payload = {
        title: this.title,
        questionType: this.questionType,
        category: this.category,
        correctChoice,
        note: this.note,
        wordBookId: this.selectedWordBookId,
        status: 1,
        options: this.options.map((o, index) => ({
          optionLabel: String.fromCharCode(65 + index),
          optionText: o.optionText,
        })),
      };
    console.log("提交前的数据:", payload);
      uni.request({
        url: "http://localhost:9192/question/add",
        method: "POST",
        header: { token: this.token },
        data: payload,
        success: (res) => {
          if (res.data.code === 1) {
            uni.showToast({ title: "提交成功！", icon: "success" });
            this.resetForm();
          } else {
            uni.showToast({ title: `提交失败：${res.data.msg}`, icon: "none" });
          }
        },
        fail: (err) => {
          uni.showToast({ title: "提交失败，请检查网络连接！", icon: "none" });
          console.error(err);
        },
      });
    }
  },
  mounted() {
    this.fetchWordBooks();
  },
};
</script>

<style scoped>
.add-question-form {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
}

input, textarea, select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

input:focus, textarea:focus, select:focus {
  border-color: #007bff;
  outline: none;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
}

textarea {
  resize: none;
}

.error-border {
  border-color: red;
}

.error-text {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.add-btn, .delete-btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.add-btn:hover, .delete-btn:hover {
  background-color: #0056b3;
}

.buttons {
  display: flex;
  gap: 10px;
}

.submit-btn {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #218838;
}

.reset-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 4px;
  cursor: pointer;
}

.reset-btn:hover {
  background-color: #c82333;
}
</style>
