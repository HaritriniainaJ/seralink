<?php
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void {
        Schema::table('contracts', function (Blueprint $table) {
            $table->boolean('client_signed')->default(false)->after('stripe_transfer_id');
            $table->boolean('freelance_signed')->default(false)->after('client_signed');
            $table->timestamp('client_signed_at')->nullable()->after('freelance_signed');
            $table->timestamp('freelance_signed_at')->nullable()->after('client_signed_at');
        });
    }
    public function down(): void {
        Schema::table('contracts', function (Blueprint $table) {
            $table->dropColumn([
                'client_signed',
                'freelance_signed',
                'client_signed_at',
                'freelance_signed_at'
            ]);
        });
    }
};