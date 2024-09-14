import React from 'react';
import { Link } from 'react-router-dom';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';

const Admin = () => {
  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={8} lg={6}>
          <Card className="shadow-sm">
            <Card.Body>
              <h2 className="text-center mb-4">관리자 페이지</h2>
              <Row className="g-4">
                <Col sm={12}>
                  <Button 
                    as={Link} 
                    to="/admin/quiz" 
                    variant="primary" 
                    className="w-100 py-3"
                  >
                    퀴즈 관리
                  </Button>
                </Col>
                <Col sm={12}>
                  <Button 
                    as={Link} 
                    to="/admin/challenges" 
                    variant="success" 
                    className="w-100 py-3"
                  >
                    챌린지 관리
                  </Button>
                </Col>
                <Col sm={12}>
                  <Button 
                    as={Link} 
                    to="/admin/notice" 
                    variant="info" 
                    className="w-100 py-3"
                  >
                    공지사항 관리
                  </Button>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Admin;