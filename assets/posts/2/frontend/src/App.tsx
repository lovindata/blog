import { QueryClientProvider, QueryClient } from "@tanstack/react-query";
import { Routes, Route, BrowserRouter, Navigate } from "react-router-dom";
import { CounterPg } from "@/modules/counter/CounterPg";

function App() {
  return (
    <main>
      <BrowserRouter>
        <QueryClientProvider client={new QueryClient()}>
          <Routes>
            <Route path="/" element={<CounterPg />} />
            {<Route path="*" element={<Navigate to="/" />} />}
          </Routes>
        </QueryClientProvider>
      </BrowserRouter>
    </main>
  );
}

export default App;
