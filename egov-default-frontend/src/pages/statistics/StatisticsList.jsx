
import { useCallback, useEffect, useRef, useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { Chart } from "react-google-charts";

import * as EgovNet from 'api/egovFetch'
import { NOTICE_BBS_ID } from 'config'
import URL from 'constants/url'

import EgovPaging from 'components/EgovPaging'
import { default as EgovLeftNav } from 'components/leftmenu/EgovLeftNavInform'

import { itemIdxByPage } from 'utils/calc'

export const options = {
    title: "처리통계",
  };

export const data = [
    ["Task", "Hours per Day"],
    ["승인 처리", 11],
    ["불승인 처리", 7],
];

function EgovNoticeList(props) {

    const location = useLocation();
	const cndRef = useRef();
    const wrdRef = useRef();
    const bbsId = location.state?.bbsId || NOTICE_BBS_ID; 

	// eslint-disable-next-line no-unused-vars
    const [searchCondition, setSearchCondition] = useState(location.state?.searchCondition || { bbsId: bbsId, pageIndex: 1, searchCnd: '0', searchWrd: '' });// 기존 조회에서 접근 했을 시 || 신규로 접근 했을 시
    const [masterBoard, setMasterBoard] = useState({});
    const [user, setUser] = useState({});
    const [paginationInfo, setPaginationInfo] = useState({});

    const [listTag, setListTag] = useState([]);

    const retrieveList = useCallback((searchCondition) => {
        console.groupCollapsed("EgovNoticeList.retrieveList()");

        const retrieveListURL = '/statistics'+EgovNet.getQueryString(searchCondition);;
        const requestOptions = {
            method: "GET",
            headers: {
                'Content-type': 'application/json',
            }
        }

        EgovNet.requestFetch(retrieveListURL,
            requestOptions,
            (resp) => {

                let mutListTag = [];
                mutListTag.push(<p className="no_data" key="0">검색된 결과가 없습니다.</p>); // 게시판 목록 초기값
                
                const resultCnt = parseInt(resp.page.totalElements);
                const currentPageNo = resp.page.number;
                const pageSize = resp.page.size;

                // 리스트 항목 구성
                resp._embedded.statistics.forEach(function (item, index) {
                    if (index === 0) mutListTag = []; // 목록 초기화
                    const listIdx = itemIdxByPage(resultCnt , currentPageNo, pageSize, index);

                    mutListTag.push(
                        <Link
                            to={{pathname: "/statistics/StatisticsDetail"}}
                            state={{
                                id: item._links.self.href.split('/').pop(),
                                searchCondition: searchCondition
}}                            key={listIdx}
                            className="list_item">
                            <div>{item._links.self.href.split('/').pop()}</div>
                            <div>{item.accidentId}</div>    
                            <div>{item.hospitalCode}</div>    
                            <div>{item.businessCode}</div>    
                            <div>{item.results}</div>    
                            <div>{item.date}</div>    
                        </Link>
                    );
                });
                setListTag(mutListTag);
            },
            function (resp) {
                console.log("err response : ", resp);
            }
        );
        console.groupEnd("EgovNoticeList.retrieveList()");
    },[]);


    useEffect(() => {
        retrieveList(searchCondition);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    console.groupEnd("EgovNoticeList");
    return (
        <div className="container">
            <div className="c_wrap">
                {/* <!-- Location --> */}
                <div className="location">
                    <ul>
                        <li><Link to={URL.MAIN} className="home">Home</Link></li>
                        <li>심사결과 통계</li>
                    </ul>
                </div>
                {/* <!--// Location --> */}

                <div className="layout">
                    <EgovLeftNav></EgovLeftNav>
                    <div className="contents NOTICE_LIST" id="contents">
                        <div className="top_tit">
                            <h1 className="tit_1">심사결과 통계</h1>
                        </div>
                        
                        <Chart
                            chartType="PieChart"
                            data={data}
                            options={options}
                            width={"100%"}
                            height={"400px"}
                            />

                        <div className="board_bot">
                            {/* <!-- Paging --> */}
                            <EgovPaging pagination={paginationInfo} moveToPage={passedPage => {
                                retrieveList({ ...searchCondition, pageIndex: passedPage, searchCnd: cndRef.current.value, searchWrd: wrdRef.current.value })
                            }} />
                            {/* <!--/ Paging --> */}
                        </div>

                        {/* <!--// 본문 --> */}
                    </div>
                </div>
            </div>
        </div>
    );
}


export default EgovNoticeList;


