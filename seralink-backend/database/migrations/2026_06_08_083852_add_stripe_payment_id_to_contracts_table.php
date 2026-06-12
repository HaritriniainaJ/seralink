<?php
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void {
        Schema::table('contracts', function (Blueprint $table) {
            $table->string('stripe_payment_id')->nullable()->after('payment_status');
            $table->string('stripe_transfer_id')->nullable()->after('stripe_payment_id');
        });
    }
    public function down(): void {
        Schema::table('contracts', function (Blueprint $table) {
            $table->dropColumn(['stripe_payment_id', 'stripe_transfer_id']);
        });
    }
};