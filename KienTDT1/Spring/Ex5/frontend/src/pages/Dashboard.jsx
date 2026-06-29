import Layout from "../components/Layout";

export default function Dashboard() {
  const cards = [
    {
      title: "Users",
      value: 120,
    },
    {
      title: "Courses",
      value: 35,
    },
    {
      title: "Students",
      value: 500,
    },
  ];

  return (
    <Layout>
      <div>
        <h1 className="dashboard-heading">Dashboard</h1>

        <div className="dashboard-grid">
          {cards.map((item) => (
            <div key={item.title} className="metric-card">
              <h2 className="metric-title">{item.title}</h2>
              <p className="metric-value">{item.value}</p>
            </div>
          ))}
        </div>
      </div>
    </Layout>
  );
}
