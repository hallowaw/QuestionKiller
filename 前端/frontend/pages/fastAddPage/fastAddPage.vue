<template>
  <view>
    <!-- 文本输入框 -->
    <textarea maxlength="2000" v-model="inputText" placeholder="请输入题目和选项"></textarea>

    <!-- 一键识别按钮 -->
    <button @click="processText">一键识别</button>

    <!-- 显示处理后的结果 -->
    <div v-if="processedQuestion">
      <pre>{{ JSON.stringify(processedQuestion, null, 2) }}</pre>
    </div>

    <!-- 确认添加和清空按钮 -->
    <div v-if="processedQuestion">
      <button @click="confirmAdd">确认添加</button>
      <button @click="clearAll">清空</button>
    </div>
  </view>
</template>

<script>
export default {
  data() {
    return {
      inputText: "", // 文本输入框的内容
      processedQuestion: null, // 处理后的题目信息
    };
  },
  methods: {
    async processText() {
      const lines = this.inputText.split("\n");
      if (lines.length < 2) {
        alert("请输入有效内容");
        return;
      }

      // 解析题目和选项
      const title = lines[0].trim();
      const options = [];
      let correctAnswer = "";
      let questionType = "单选题";

      for (const line of lines.slice(1)) {
        // 支持 A-Z 字母，最多识别 10 个选项
        const match = /^[A-J]\./.exec(line); // 修改了正则，支持到 J（即 10 个选项）
        if (match) {
          const optionLabel = match[0].charAt(0); // 获取选项字母 A-J
          const optionText = line.slice(3).trim(); // 选项内容
          options.push({ optionLabel, optionText });
        } else if (line.startsWith("Correct Answer:")) {
          correctAnswer = line.replace("Correct Answer:", "").trim();
          questionType = correctAnswer.includes(",") ? "多选题" : "单选题";
        }
      }

      // 获取默认词书 ID
      try {
        const res = await this.getWordBookId();
        const wordBookId = res.data.data; // 默认使用 1

        // 构造处理后的问题对象
        this.processedQuestion = {
          title,
          questionType,
          category: "默认分类",
          correctChoice: correctAnswer,
          note: "",
          wordBookId,
          status: 1,
          options,
        };
      } catch (error) {
        console.error("获取词书 ID 失败:", error);
      }
    },

    // 调用后端接口获取默认词书 ID
    async getWordBookId() {
      return new Promise((resolve, reject) => {
        uni.request({
          url: "http://localhost:9192/user/getDefaultWordBookIdByCurrentID", // 替换为实际的后端接口地址
          method: "GET",
          header: {
            token: uni.getStorageSync("token"),
          },
          success: (res) => {
            if (res.statusCode === 200 && res.data) {
              resolve(res);
            } else {
              reject(new Error("获取词书 ID 失败"));
            }
          },
          fail: (err) => {
            reject(err);
          },
        });
      });
    },

    confirmAdd() {
      if (!this.processedQuestion) {
        alert("没有可提交的数据！");
        return;
      }

      // 提交数据到后端
      uni.request({
        url: "http://localhost:9192/question/add", // 替换为实际的后端接口地址
        method: "POST",
        header: {
          token: uni.getStorageSync("token"),
        },
        data: this.processedQuestion,
        success: (res) => {
          if (res.statusCode === 200) {
            uni.showToast({ title: "提交成功", icon: "success" });
            this.clearAll(); // 清空数据
          } else {
            uni.showToast({ title: "提交失败，请检查后端接口", icon: "none" });
          }
        },
        fail: (err) => {
          console.error(err);
          uni.showToast({ title: "请求失败，请检查网络连接", icon: "none" });
        },
      });
    },

    clearAll() {
      this.inputText = ""; // 清空输入框
      this.processedQuestion = null; // 清空处理结果
    },
  },
};
</script>

<style scoped>
/* 样式和之前一致 */
view {
  padding: 20px;
  font-family: Arial, sans-serif;
}

textarea {
  width: 100%;
  height: 100px;
  margin-bottom: 10px;
  font-size: 14px;
  padding: 10px;
}

button {
  margin: 5px 5px 10px 0;
  padding: 10px 20px;
  font-size: 14px;
  cursor: pointer;
}

pre {
  background: #f4f4f4;
  padding: 10px;
  border: 1px solid #ddd;
  margin-top: 10px;
}
</style>
