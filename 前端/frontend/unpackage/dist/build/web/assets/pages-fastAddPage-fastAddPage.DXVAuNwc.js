import{r as e,k as t,s as o,d as s,w as n,i as r,o as l,e as i,f as a,l as c,D as d,t as u,g as p,A as h,j as f}from"./index-iGyM-cqh.js";import{_ as k}from"./_plugin-vue_export-helper.BCo6x5W8.js";const m=k({data:()=>({inputText:"",processedQuestion:null}),methods:{async processText(){const e=this.inputText.split("\n");if(e.length<2)return void alert("请输入有效内容");const t=e[0].trim(),o=[];let s="",n="单选题";for(const l of e.slice(1)){const e=/^[A-J]\./.exec(l);if(e){const t=e[0].charAt(0),s=l.slice(3).trim();o.push({optionLabel:t,optionText:s})}else l.startsWith("Correct Answer:")&&(s=l.replace("Correct Answer:","").trim(),n=s.includes(",")?"多选题":"单选题")}try{const e=(await this.getWordBookId()).data.data;this.processedQuestion={title:t,questionType:n,category:"默认分类",correctChoice:s,note:"",wordBookId:e,status:1,options:o}}catch(r){console.error("获取词书 ID 失败:",r)}},getWordBookId:async()=>new Promise(((o,s)=>{e({url:"http://localhost:9192/user/getDefaultWordBookIdByCurrentID",method:"GET",header:{token:t("token")},success:e=>{200===e.statusCode&&e.data?o(e):s(new Error("获取词书 ID 失败"))},fail:e=>{s(e)}})})),confirmAdd(){this.processedQuestion?e({url:"http://localhost:9192/question/add",method:"POST",header:{token:t("token")},data:this.processedQuestion,success:e=>{200===e.statusCode?(o({title:"提交成功",icon:"success"}),this.clearAll()):o({title:"提交失败，请检查后端接口",icon:"none"})},fail:e=>{console.error(e),o({title:"请求失败，请检查网络连接",icon:"none"})}}):alert("没有可提交的数据！")},clearAll(){this.inputText="",this.processedQuestion=null}}},[["render",function(e,t,o,k,m,x){const C=h,T=f,A=r;return l(),s(A,null,{default:n((()=>[i(C,{maxlength:"2000",modelValue:m.inputText,"onUpdate:modelValue":t[0]||(t[0]=e=>m.inputText=e),placeholder:"请输入题目和选项"},null,8,["modelValue"]),i(T,{onClick:x.processText},{default:n((()=>[a("一键识别")])),_:1},8,["onClick"]),m.processedQuestion?(l(),c("div",{key:0},[d("pre",null,u(JSON.stringify(m.processedQuestion,null,2)),1)])):p("",!0),m.processedQuestion?(l(),c("div",{key:1},[i(T,{onClick:x.confirmAdd},{default:n((()=>[a("确认添加")])),_:1},8,["onClick"]),i(T,{onClick:x.clearAll},{default:n((()=>[a("清空")])),_:1},8,["onClick"])])):p("",!0)])),_:1})}],["__scopeId","data-v-db780c23"]]);export{m as default};
