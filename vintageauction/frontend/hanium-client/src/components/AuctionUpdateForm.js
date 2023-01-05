import { React, useState, useRef} from "react";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const AuctionUpdateForm =({memberObj}) => {
    const [title, setTitle] = useState("");
    const [detail, setDetail] = useState("");
    const [itemName, setItemName] = useState("");
    const [price, setPrice] = useState(0);
    const [duration, setDuration] = useState(0);
    const [attachment, setAttachment] = useState(null);
    const [error, setError] = useState(null);
    const [file, setFile] = useState(null);
    const [category, setCategory] = useState("");
    const fileInput = useRef();
    const formData = new FormData();
    const navigate = useNavigate();

    const onChange = (event) => {
        const {target: {name, value}} = event;
        if(name === "title"){
            setTitle(value)
        }else if(name == "detail"){
            setDetail(value)
        }else if(name === "item-name"){
            setItemName(value)
        }else if(name === "start_price"){
            setPrice(value.replace(/[^0-9]/g, ''))
        }else if(name === "duration"){
            setDuration(value.replace(/[^0-9]/g, ''));
        }
    }

    const onCategoryChange = (event) => {
        setCategory(event.target.value);
    }

    const onFileChange = (event) => {
        // 이미지 입력 시 url을 읽는 함수
        const {target:{files}} = event;
        const theFile = files[0];
        const reader = new FileReader();
        reader.onloadend = (finishEvent) => {
            const {currentTarget: { result }} = finishEvent
            setAttachment(result)
        }
        reader.readAsDataURL(theFile);
        setFile(theFile);
        console.log(theFile);
    }

    const onSubmit = async (event) => {
        // 중고 상품을 업로드하는 함수
        event.preventDefault();
        // 입력받은 데이터를 객체에 담아
        // 회원가입 api에 post 요청
        formData.append("imageFiles", file);
        formData.append("auctionTitle", title);
        formData.append("itemName", itemName);
        formData.append("itemBidPrice", Number(price));
        formData.append("duringTime", Number(duration));
        formData.append("auctionDetail", detail);
        formData.append("itemCategory", category);
        
        console.log(category)
        await axios.post('/api/auction/new/', 
        formData, {headers: {
            'Content-Type': "multipart/form-data"
        }})
        .then(response => {
            console.log(response.data);
            navigate("/");
        })
        .catch(error => console.log(error.response.data))
    };
    return (
        <>
        준비중입니다.
        </>
    )
}
export default AuctionUpdateForm;