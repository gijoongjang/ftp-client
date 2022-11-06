# SpringBoot FTPClient

### Front With Axios
### file upload
```js
const paramData = {
  filePath : "/A/B/C",
  fileName : "F.pdf",
};

const formData = new FormData();
formData.append('files', element.files[0]);
formData.append('dto', new Blob([JSON.stringify(paramData)], {type: "application/json"}));

axios({
  url: 'server-url',
  method: 'POST',
  data: formData
})
.then(response => {})
```


### file download
```js
axios({
    url: 'server-url',
    method: 'POST',
    data:{
      filePath: '/A/B/C/',
      fileName: 'D.pdf'
    }
    responseType:'blob'
    headers: {'Accept': 'application/octet-stream'}
})
.then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data], {type: response.headers['content-type']}));
    const link = document.createElment('a');
    link.href = url;
    link.setAttribute('download', 'fileName');
    document.body.appendChild(link);
    link.click();
});
```
