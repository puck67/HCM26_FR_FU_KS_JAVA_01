export default function Footer() {
  return (
    <footer className="bg-inverse-surface text-inverse-on-surface py-8 px-8 border-t border-outline-variant mt-auto" data-purpose="main-footer">
      <div className="max-w-6xl mx-auto flex flex-col items-center gap-6">
        <div className="text-center">
          <p className="text-sm opacity-90">© 2023 - All rights reserved. Built with Spring Framework</p>
        </div>
        
        {/* Social Media Icons */}
        <div className="flex items-center gap-6">
          <a aria-label="Facebook" className="hover:text-primary transition-colors duration-200" href="#">
            <svg className="w-6 h-6 fill-current" viewBox="0 0 24 24">
              <path d="M22 12c0-5.52-4.48-10-10-10S2 6.48 2 12c0 4.84 3.44 8.87 8 9.8V15H8v-3h2V9.5C10 7.57 11.57 6 13.5 6H16v3h-2c-.55 0-1 .45-1 1v2h3v3h-3v6.95c5.05-.5 9-4.76 9-9.95z"></path>
            </svg>
          </a>
          <a aria-label="Twitter" className="hover:text-primary transition-colors duration-200" href="#">
            <svg className="w-6 h-6 fill-current" viewBox="0 0 24 24">
              <path d="M23 3a10.9 10.9 0 0 1-3.14 1.53 4.48 4.48 0 0 0-7.86 3v1A10.66 10.66 0 0 1 3 4s-4 9 5 13a11.64 11.64 0 0 1-7 2c9 5 20 0 20-11.5a4.5 4.5 0 0 0-.08-.83A7.72 7.72 0 0 0 23 3z"></path>
            </svg>
          </a>
          <a aria-label="Instagram" className="hover:text-primary transition-colors duration-200" href="#">
            <svg className="w-6 h-6 fill-current" viewBox="0 0 24 24">
              <path d="M12 2c2.717 0 3.056.01 4.122.058 1.066.048 1.79.218 2.427.465a4.902 4.902 0 0 1 1.772 1.153 4.902 4.902 0 0 1 1.153 1.772c.247.637.417 1.361.465 2.427.048 1.066.058 1.405.058 4.122s-.01 3.056-.058 4.122c-.048 1.066-.218 1.79-.465 2.427a4.902 4.902 0 0 1-1.153 1.772 4.902 4.902 0 0 1-1.772 1.153c-.637.247-1.361.417-2.427.465-1.066.048-1.405.058-4.122.058s-3.056-.01-4.122-.058c-1.066-.048-1.79-.218-2.427-.465a4.902 4.902 0 0 1-1.772-1.153 4.902 4.902 0 0 1-1.153-1.772c-.247-.637-.417-1.361-.465-2.427-.047-1.066-.058-1.405-.058-4.122s.01-3.056.058-4.122c.048-1.066.218-1.79.465-2.427a4.902 4.902 0 0 1 1.153-1.772A4.902 4.902 0 0 1 5.45 2.523c.637-.247 1.361-.417 2.427-.465C8.944 2.01 9.283 2 12 2zm0 5a5 5 0 1 0 0 10 5 5 0 0 0 0-10zm6.5-.25a1.25 1.25 0 0 0-2.5 0 1.25 1.25 0 0 0 2.5 0zM12 9a3 3 0 1 1 0 6 3 3 0 0 1 0-6z"></path>
            </svg>
          </a>
          <a aria-label="YouTube" className="hover:text-primary transition-colors duration-200" href="#">
            <svg className="w-6 h-6 fill-current" viewBox="0 0 24 24">
              <path d="M23.498 6.186a3.016 3.016 0 0 0-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 0 0 .502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 0 0 2.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 0 0 2.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z"></path>
            </svg>
          </a>
        </div>
      </div>
    </footer>
  );
}
