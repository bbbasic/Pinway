import API from "./api";

export const addPost = async (image, data) => {
    const form = new FormData();
    form.append('postDTO', JSON.stringify(data));
    form.append('image', image[0]);

    const response = await API.post(`/api/post/add`,
    form,
    { headers: { Authorization: `Bearer ${localStorage.getItem('Bearer')}`, 'Content-Type' : `multipart/form-data`  } });
    return response.data;
}

export const addComment = async (data) => {
    const response = await API.post('api/comment/add', data, { headers: { Authorization: `Bearer ${localStorage.getItem('Bearer')}` } } );
    return response;
}

export const addLike = async (data) => {
    const response = await API.post('/api/like/add', data,     { headers: { Authorization: `Bearer ${localStorage.getItem('Bearer')}`, 'Content-Type' : `application/json`  } });
    return response;
}


export const getPostById = async (id) => {
    const response = await API.get('/api/post/findByIds?Ids='+ id,
    { headers: { Authorization: `Bearer ${localStorage.getItem('Bearer')}` } })
    return response.data[0];
}

export const getCommentsForPost = async (postId) => {
    const response = await API.get(`api/comment/getByPost?PostId=${postId}`,{ headers: { Authorization: `Bearer ${localStorage.getItem('Bearer')}` } })
    return response.data
}


