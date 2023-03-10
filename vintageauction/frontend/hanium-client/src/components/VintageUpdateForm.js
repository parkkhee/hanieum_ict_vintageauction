import { React, useState, useRef} from "react";
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import "../css/Vintage.css"
const VintageUpdateForm =({itemInfo, vintageId}) => {
    const [title, setTitle] = useState(itemInfo.title);
    const [detail, setDetail] = useState(itemInfo.detail);
    const [itemName, setItemName] = useState(itemInfo.itemName);
    const [price, setPrice] = useState(itemInfo.itemPrice);
    const [attachment, setAttachment] = useState(null);
    const [error, setError] = useState(null);
    const [file, setFile] = useState(null);
    const [category, setCategory] = useState(itemInfo.itemCategory);
    const fileInput = useRef();
    const formData = new FormData();
    const navigate = useNavigate();

    const onFileChange = (event) => {
        // 이미지 입력 시 url을 읽는 함수
        const {target:{files}} = event;
        const theFile = files[0];
        const reader = new FileReader();
        reader.readAsDataURL(theFile);
        setFile(theFile);
        console.log(theFile);
    }

    const onChange = (event) => {
        const {target: {name, value}} = event;
        
        if(name === "title"){
            setTitle(value)
        }else if(name == "detail"){
            setDetail(value)
        }else if(name === "item-name"){
            setItemName(value)
        }else if(name === "price"){
            setPrice(value)
        }
    }

    const onCategoryChange = (event) => {
        setCategory(event.target.value);
    }

    const editPost = async (event) => {
        console.log(itemInfo.vintageId)
        
        // 중고 상품을 업로드하는 함수
        event.preventDefault();
        // 입력받은 데이터를 객체에 담아
        // 회원가입 api에 post 요청
        formData.append("imageFiles", file);
        formData.append("vintageTitle", title);
        formData.append("itemName", itemName);
        formData.append("itemPrice", Number(price));
        formData.append("vintageDetail", detail);
        formData.append("itemCategory", category);
        await axios.post(`/api/vintage/${vintageId}/edit`, 
        formData, {headers: {
            'Content-Type': "multipart/form-data"
        }})
        .then(response => {
            console.log(response.data);
            navigate(`/vintages`);
        })
        .catch(error => console.log(error.response.data))
    };
    return (
        <>
            <form onSubmit={editPost} className="vintage-upload-form">
                <div className="image-form">
                    <img className="item-image"
                    src={itemInfo.storeFileName[0] ? require(`../itemImages/${itemInfo.storeFileName[0]}`):require('../img/temp.png')}/>
                    <br/>
                    <label htmlFor="item-image">이미지 등록</label>
                    <input 
                    type="file"
                    name="item-image"
                    id="item-image"
                    onChange={onFileChange}
                    ref={fileInput}
                    />
                </div>
                <div className="text-form">
                    <label htmlFor="title-input">제목</label>
                    <input 
                    name="title"
                    placeholder="title" 
                    required 
                    value={title}
                    onChange={onChange}
                    className="title-input"
                    id="title-input"/>
                    <br/>
                    <label htmlFor="item-name-input">상품명</label>
                    <input 
                    name="item-name"
                    placeholder="item name" 
                    required 
                    value={itemName}
                    onChange={onChange}
                    className="item-name-input"
                    id="item-name-input"/>
                    <br/>
                    <label htmlFor="price-input">가격</label>
                    <input 
                    name="price"
                    placeholder="price" 
                    required 
                    value={price}
                    onChange={onChange}
                    className="price-input"
                    id="price-input"/>
                    <br/>
                    <label htmlFor="detail-input">설명</label>
                    <input 
                    name="detail"
                    placeholder="detail" 
                    required 
                    value={detail}
                    onChange={onChange}
                    className="detail-input"
                    id="detail-input"/>
                    <br/>
                    <label htmlFor="category">카테고리</label>
                    <select name="category" id="category" onChange={onCategoryChange}>
                        <option >category</option>
                        <option value="clothes">의류</option>
                        <option value="test1">test1</option>
                        <option value="test2">test2</option>
                        <option value="test3">test3</option>
                        <option value="test4">test4</option>
                        <option value="test5">test5</option>
                    </select>
                </div>
                <input type="submit" 
                className="vintage-submit"
                value="저장"/>
                {error && <span className="authError">{error}</span>}
            </form>
        </>
    )
}
export default VintageUpdateForm;