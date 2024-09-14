import React from 'react';
import { Pagination } from 'react-bootstrap';

const CustomPagination = ({ currentPage, totalPages, onPageChange }) => {
  const pageNumbers = [];
  let startPage, endPage;

  if (totalPages <= 10) {
    // 총 페이지가 10 이하면 모든 페이지 표시
    startPage = 1;
    endPage = totalPages;
  } else {
    // 현재 페이지를 중심으로 앞뒤로 4페이지씩 표시
    if (currentPage <= 6) {
      startPage = 1;
      endPage = 10;
    } else if (currentPage + 4 >= totalPages) {
      startPage = totalPages - 9;
      endPage = totalPages;
    } else {
      startPage = currentPage - 5;
      endPage = currentPage + 4;
    }
  }

  for (let i = startPage; i <= endPage; i++) {
    pageNumbers.push(i);
  }

  return (
    <div className="d-flex justify-content-center"> {/* 이 div를 추가하여 중앙 정렬 */}
      <Pagination>
        <Pagination.First onClick={() => onPageChange(1)} disabled={currentPage === 1} />
        <Pagination.Prev onClick={() => onPageChange(currentPage - 1)} disabled={currentPage === 1} />
        
        {pageNumbers.map(number => (
          <Pagination.Item
            key={number}
            active={number === currentPage}
            onClick={() => onPageChange(number)}
          >
            {number}
          </Pagination.Item>
        ))}

        <Pagination.Next onClick={() => onPageChange(currentPage + 1)} disabled={currentPage === totalPages} />
        <Pagination.Last onClick={() => onPageChange(totalPages)} disabled={currentPage === totalPages} />
      </Pagination>
    </div>
  );
};

export default CustomPagination;