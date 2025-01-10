<template>
  <view>
    <p>请严格保持格式一致，可以使用AI工具调整格式一致，请不要添加或减少元素，否则无法识别，注意Correct Answer: A,B  这里的,是英文的</p>
    
    <p>单选题一键添加示例：</p>
    <view class="textarea-container">
      <button class="copy-btn" @click="copyToClipboard(singleExample)">复制</button>
      <textarea 
        readonly 
        :value="singleExample">
      </textarea>
    </view>

    <p>多选题一键添加示例：</p>
    <view class="textarea-container">
      <button class="copy-btn" @click="copyToClipboard(multiExample)">复制</button>
      <textarea 
        readonly 
        :value="multiExample">
      </textarea>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      singleExample: `What is your name? 
A. 1
B. 2
C. 3
D. 4
Correct Answer: C`,
      multiExample: `What is your name? 
A. 1
B. 2
C. 3
D. 4
Correct Answer: A,B`
    };
  },
  methods: {
    copyToClipboard(text) {
      // 使用 clipboard API 实现复制
      if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(() => {
          alert("复制成功！");
        }).catch(() => {
          alert("复制失败，请手动复制！");
        });
      } else {
        alert("当前浏览器不支持复制功能，请手动复制！");
      }
    }
  }
};
</script>

<style>
.textarea-container {
	  width: 80%;
  position: relative;
  margin-bottom: 20px;
}

textarea {
  width: 75%;
  height: auto;
  min-height: 150px;
  resize: none;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 8px;
  background-color: #f9f9f9;
  font-family: inherit;
  font-size: inherit;
  white-space: pre-wrap; /* 保证换行符显示 */
  overflow: auto;
}

.copy-btn {
  position: absolute;
  top: 10px; /* 调整位置到文本框内部的右上角 */
  right: 10px;
  padding: 5px 10px;
  font-size: 12px;
  background-color: #ccc; /* 背景色改为灰色 */
  color: black;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.copy-btn:hover {
  background-color: #bbb; /* 悬停时变深一点 */
}

p {
  margin: 10px 0;
  font-weight: bold;
}
</style>