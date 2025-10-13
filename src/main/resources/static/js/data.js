// products data (kamu adatok)
const PRODUCTS = [
  {id: 'p1', name: 'Sós perec', price: 450, type: 'pretzel', img: 'assets/img/sample1.jpg', desc: 'Frissen sült, ropogós sós perec.'},
  {id: 'p2', name: 'Csokis perec', price: 520, type: 'pretzel', img: 'assets/img/sample2.jpg', desc: 'Csokoládés mázzal.'},
  {id: 'p3', name: 'Fahéjas csiga', price: 390, type: 'dessert', img: 'assets/img/sample3.jpg', desc: 'Illatos fahéjas csiga.'},
  {id: 'p4', name: 'Édes muffin', price: 420, type: 'dessert', img: 'assets/img/sample4.jpg', desc: 'Puha, édes muffin.'},
  {id: 'm1', name: 'Gülü eper plüss', price: 1990, type: 'merch', img: 'assets/img/sample5.jpg', desc: 'Puha epres plüssfigura.'},
  {id: 'm2', name: 'Pretzel bögre', price: 2490, type: 'merch', img: 'assets/img/sample6.jpg', desc: 'Kerámia bögre logóval.'},
  {id: 'm3', name: 'Logós póló', price: 3990, type: 'merch', img: 'assets/img/sample7.jpg', desc: 'Pamut póló a logóval.'}
];

function findProductById(id){ return PRODUCTS.find(p=>p.id===id); }
