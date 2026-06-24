import { useState } from "react";
import { Box, TrendingUp, AlertCircle } from "lucide-react";
import { CrudTable } from "../components/CrudTable";
import { CrudActionBar } from "../components/CrudActionBar";
import { ConfirmModal } from "../components/ConfirmModal";
import { ImportModal } from "../components/ImportModal";
import { CrudFormModal, type FieldConfig } from "../components/CrudFormModal";
import { ViewModal } from "../components/ViewModal";
import { StatCard } from "../components/StatCard";

type Product = { sku: string; name: string; price: string; stock: number };

export default function ProductPage() {
  const [products, setProducts] = useState<Product[]>([
    { sku: "P-001", name: "Laptop Pro", price: "$1200", stock: 45 },
    { sku: "P-002", name: "Wireless Mouse", price: "$40", stock: 120 },
    { sku: "P-003", name: "Mechanical Keyboard", price: "$150", stock: 30 },
  ]);

  const [isAddOpen, setIsAddOpen] = useState(false);
  const [isImportOpen, setIsImportOpen] = useState(false);
  const [viewingProduct, setViewingProduct] = useState<Product | null>(null);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [deletingProduct, setDeletingProduct] = useState<Product | null>(null);

  const totalProducts = products.length;
  const totalStock = products.reduce((acc, p) => acc + p.stock, 0);
  const lowStock = products.filter(p => p.stock < 50).length;

  const fields: FieldConfig[] = [
    { name: "sku", label: "SKU", type: "text" },
    { name: "name", label: "Product Name", type: "text" },
    { name: "price", label: "Price", type: "text" },
    { name: "stock", label: "Stock Quantity", type: "number" }
  ];

  const handleExport = () => {
    const csvContent = "data:text/csv;charset=utf-8," 
      + ["sku", "name", "price", "stock"].join(",") + "\\n"
      + products.map(p => `${p.sku},"${p.name}","${p.price}",${p.stock}`).join("\\n");
    const link = document.createElement("a");
    link.setAttribute("href", encodeURI(csvContent));
    link.setAttribute("download", "products_export.csv");
    link.click();
  };

  const handleSaveAdd = (data: any) => {
    setProducts([...products, data as Product]);
    setIsAddOpen(false);
  };

  const handleSaveEdit = (data: any) => {
    setProducts(products.map(p => p.sku === data.sku ? data : p));
    setEditingProduct(null);
  };

  const handleDelete = () => {
    if (!deletingProduct) return;
    setProducts(products.filter(p => p.sku !== deletingProduct.sku));
    setDeletingProduct(null);
  };

  return (
    <div className="w-full max-w-[1600px] mx-auto animate-fadeIn flex flex-col gap-5">
      <CrudActionBar
        onAdd={() => setIsAddOpen(true)}
        onImport={() => setIsImportOpen(true)}
        onExport={handleExport}
      />
      
      <div className="bg-white border border-slate-200 rounded-2xl shadow-sm overflow-hidden">
        <CrudTable
          data={products}
          onView={(p) => setViewingProduct(p as Product)}
          onEdit={(p) => setEditingProduct(p as Product)}
          onDelete={(p) => setDeletingProduct(p as Product)}
        />
      </div>

      <ViewModal isOpen={!!viewingProduct} onClose={() => setViewingProduct(null)} title="Product Details" data={viewingProduct} />
      <CrudFormModal isOpen={isAddOpen} onClose={() => setIsAddOpen(false)} onSave={handleSaveAdd} title="Add New Product" fields={fields} />
      <CrudFormModal isOpen={!!editingProduct} onClose={() => setEditingProduct(null)} onSave={handleSaveEdit} title="Edit Product" fields={fields} initialData={editingProduct} />
      <ImportModal isOpen={isImportOpen} onClose={() => setIsImportOpen(false)} onImport={() => alert("File imported!")} />
      <ConfirmModal isOpen={!!deletingProduct} onClose={() => setDeletingProduct(null)} onConfirm={handleDelete} title="Delete Product" message={`Are you sure you want to delete product "${deletingProduct?.name}"?`} />
    </div>
  );
}
